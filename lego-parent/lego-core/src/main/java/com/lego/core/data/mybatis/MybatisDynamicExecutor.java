package com.lego.core.data.mybatis;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lego.core.util.MybatisUtils;
import com.lego.core.util.StringUtil;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class MybatisDynamicExecutor {

    private static final String COUNT_SQL = "SELECT COUNT(1) CC FROM ({0})";

    public long selectCount(SqlSessionTemplate sqlSessionTemplate, String sql, Map<String, Object> parameter) {
        SqlSession sqlSession = getSqlSession(sqlSessionTemplate);
        try {
            String countSql = StringUtil.format(COUNT_SQL, sql);
            List<Map<String, BigDecimal>> select = MybatisUtils.select(sqlSession, countSql, parameter);
            if (select.isEmpty()) {
                return 0;
            }
            return select.get(0).get("CC").longValue();
        } finally {
            closeSqlSession(sqlSession, sqlSessionTemplate);
        }
    }

    public <M> IPage<M> selectPage(SqlSessionTemplate sqlSessionTemplate, String sql, Map<String, Object> parameter, int pageSize, int pageIndex) {
        SqlSession sqlSession = getSqlSession(sqlSessionTemplate);
        try {
            return MybatisUtils.selectPage(sqlSession, sql, parameter, pageSize, pageIndex);
        } finally {
            closeSqlSession(sqlSession, sqlSessionTemplate);
        }
    }

    public <M> List<M> select(SqlSessionTemplate sqlSessionTemplate, String sql, Map<String, Object> parameter) {
        SqlSession sqlSession = getSqlSession(sqlSessionTemplate);
        try {
            return MybatisUtils.select(sqlSession, sql, parameter);
        } finally {
            closeSqlSession(sqlSession, sqlSessionTemplate);
        }
    }

    /**
     * 获取sqlSession
     */
    public SqlSession getSqlSession(SqlSessionTemplate sqlSessionTemplate) {
        return SqlSessionUtils.getSqlSession(sqlSessionTemplate.getSqlSessionFactory(),
            sqlSessionTemplate.getExecutorType(), sqlSessionTemplate.getPersistenceExceptionTranslator());
    }

    /**
     * 关闭sqlSession
     */
    public void closeSqlSession(SqlSession session, SqlSessionTemplate sqlSessionTemplate) {
        SqlSessionUtils.closeSqlSession(session, sqlSessionTemplate.getSqlSessionFactory());
    }
}
