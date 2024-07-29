package com.lego.job.core.conf;

import com.lego.core.common.ServiceStartType;
import com.lego.core.util.StringUtil;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class JobAdminStartConditional implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        String startType = environment.getProperty("lego.start-type");
        String adminAddresses = environment.getProperty("xxl.job.admin.addresses");
        return ServiceStartType.microservice.equals(startType) || StringUtil.isNotBlank(adminAddresses);
    }
}
