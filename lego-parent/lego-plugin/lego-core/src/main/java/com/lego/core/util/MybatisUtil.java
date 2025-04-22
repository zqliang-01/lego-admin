package com.lego.core.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lego.core.exception.BusinessException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@UtilityClass
public class MybatisUtil {

    /**
     * 普通查询
     *
     * @param sql       sql文本
     * @param parameter 参数
     */
    public static <M> List<M> select(SqlSession session, String sql, Map<String, Object> parameter) {
        String statementId = buildSelectStatementId(session, sql);
        return session.selectList(statementId, parameter);
    }

    /**
     * 分页查询
     *
     * @param sql       sql文本
     * @param parameter 参数
     * @param pageSize  每页记录数
     * @param pageIndex 页码，从1开始
     * @return
     */
    public static <M> IPage<M> selectPage(SqlSession session, String sql, Map<String, Object> parameter, int pageSize, int pageIndex) {
        BusinessException.check(pageIndex >= 1, "页码必须从1开始");
        BusinessException.check(pageSize >= 0, "每页记录数必须大于或者等于0");

        String statementId = buildSelectStatementId(session, sql);

        IPage<M> result = new Page<M>(pageIndex, pageSize);
        if (pageSize > 0) {
            parameter.put("page", result);
        }

        List<M> list = session.selectList(statementId, parameter);
        if (list instanceof IPage) {
            IPage<M> pageList = (IPage<M>) list;
            result.setRecords(pageList.getRecords());
            result.setTotal(pageList.getTotal());
        } else {
            result.setRecords(list);
        }

        return result;
    }

    private static String buildSelectStatementId(SqlSession session, String sql) {
        String statementId = "Lego-SQL." + sql.hashCode();
        log.debug("语句id: {}", statementId);
        final Configuration config = session.getConfiguration();

        if (!config.hasStatement(statementId, false)) {
            LanguageDriver langDriver = config.getDefaultScriptingLanguageInstance();
            SqlSource sqlSource = langDriver.createSqlSource(config, "<script>" + sql + "</script>", Map.class);

            MappedStatement.Builder statementBuilder = new MappedStatement.Builder(config, statementId, sqlSource, SqlCommandType.SELECT);

            ArrayList<ResultMap> resultMaps = new ArrayList<ResultMap>();
            resultMaps.add(new ResultMap.Builder(config, "LegoResultMap", LinkedHashMap.class, new ArrayList<ResultMapping>()).build());

            statementBuilder.resultMaps(resultMaps);
            config.addMappedStatement(statementBuilder.build());
        } else {
            log.debug("语句已经存在:\n{}", sql);
        }
        return statementId;
    }

    public static void insert(SqlSession session, String sql, Map<String, Object> parameter) {

        String statementId = "Lego-SQL." + sql.hashCode();
        log.debug("语句id: {}", statementId);
        Configuration config = session.getConfiguration();

        if (!config.hasStatement(statementId, false)) {
            LanguageDriver langDriver = config.getDefaultScriptingLanguageInstance();
            SqlSource sqlSource = langDriver.createSqlSource(config, String.format("<script>%s</script>", sql), Map.class);

            MappedStatement.Builder builder = new MappedStatement.Builder(config, statementId, sqlSource, SqlCommandType.INSERT);
            config.addMappedStatement(builder.build());
        } else {
            log.debug("语句已经存在:\n{}", sql);
        }
        session.insert(statementId, parameter);
    }

    public static void delete(SqlSession session, String sql, Map<String, Object> parameter) {
        String statementId = "Lego-SQL." + sql.hashCode();
        log.debug("语句id: {}", statementId);
        Configuration config = session.getConfiguration();

        if (!config.hasStatement(statementId, false)) {
            log.info("创建动态语句:\n{}", sql);

            LanguageDriver langDriver = config.getDefaultScriptingLanguageInstance();
            SqlSource sqlSource = langDriver.createSqlSource(config, String.format("<script>%s</script>", sql), Map.class);

            MappedStatement.Builder builder = new MappedStatement.Builder(config, statementId, sqlSource, SqlCommandType.DELETE);
            config.addMappedStatement(builder.build());
        } else {
            log.debug("语句已经存在:\n{}", sql);
        }
        session.delete(statementId, parameter);
    }
}
