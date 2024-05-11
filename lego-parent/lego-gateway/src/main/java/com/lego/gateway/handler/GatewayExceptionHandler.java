package com.lego.gateway.handler;

import cn.dev33.satoken.exception.InvalidContextException;
import cn.dev33.satoken.exception.NotLoginException;
import com.lego.core.common.ExceptionEnum;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.JsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关统一异常处理
 */
@Slf4j
@Order(-1)
@Configuration
public class GatewayExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GatewayExceptionHandler(ErrorAttributes errorAttributes, ApplicationContext applicationContext,
                                   ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable ex = getError(request);
        String msg = ex.getMessage();
        Integer code = ExceptionEnum.UNKNOW_ERROR.getCode();
        if (ex instanceof NotLoginException) {
            msg = ExceptionEnum.SESSION_INVALID.getMsg();
            code = ExceptionEnum.SESSION_INVALID.getCode();
        } else if (ex instanceof InvalidContextException) {
        } else if (ex instanceof ResponseStatusException) {
        } else if (ex instanceof NotFoundException) {
            msg = StringUtil.format("下游服务异常，异常原因[{0}]", msg);
        } else if (ex instanceof BusinessException) {
            BusinessException bx = (BusinessException) ex;
            code = bx.getCode();
            msg = bx.getMessage();
        } else {
            msg = "全局未知异常，请联系技术人员处理！";
        }

        BodyInserter<JsonResponse, ReactiveHttpOutputMessage> result = BodyInserters.fromValue(JsonResponse.failed(code, msg));
        return ServerResponse.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(result);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }
        if (ex instanceof NotLoginException) {
            return super.handle(exchange, ex);
        }
        log.error(StringUtil.format("[网关异常处理]请求路径:{0}", exchange.getRequest().getPath()), ex);
        return super.handle(exchange, ex);
    }
}
