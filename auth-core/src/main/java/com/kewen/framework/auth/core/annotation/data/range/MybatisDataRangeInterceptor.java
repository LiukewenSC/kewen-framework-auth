package com.kewen.framework.auth.core.annotation.data.range;


import com.kewen.framework.auth.core.annotation.AuthDataRange;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.sql.Connection;
import java.util.Properties;

/**
 * MyBatis 允许你在映射语句执行过程中的某一点进行拦截调用。默认情况下，MyBatis 允许使用插件来拦截的方法调用包括：
 * <p>
 * Executor (update, query, flushStatements, commit, rollback, getTransaction, close, isClosed)
 * ParameterHandler (getParameterObject, setParameters)
 * ResultSetHandler (handleResultSets, handleOutputParameters)
 * StatementHandler (prepare, parameterize, batch, update, query)
 * 这些类中方法的细节可以通过查看每个方法的签名来发现，或者直接查看 MyBatis 发行包中的源代码。 如果你想做的不仅仅是监控方法的调用，那么你最好相当了解要重写的方法的行为。
 * 因为在试图修改或重写已有方法的行为时，很可能会破坏 MyBatis 的核心模块。 这些都是更底层的类和方法，所以使用插件的时候要特别当心。
 * <p>
 * 通过 MyBatis 提供的强大机制，使用插件是非常简单的，只需实现 Interceptor 接口，并指定想要拦截的方法签名即可
 */
@Slf4j
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class MybatisDataRangeInterceptor implements Interceptor,ApplicationContextAware {

    public MybatisDataRangeInterceptor() {
    }

    SqlAuthInject sqlAuthInject;
    /**
     * Mybatis的插件先于spring容器的完全初始化 ,因此不能直接通过注入等方式获取到容器中的Bean，需要在第一次使用的时候才进行初始化
     * 可以先加载ApplicationContext，再使用时加载AnnotationAuthHandler，
     * 也可以注入的时候添加@Lazy注解
     * @Autowired
     * @Lazy
     * private AnnotationAuthHandler annotationAuthHandler
     */
    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    /**
     * 获取SQL权限注入器
     * 双重检测锁方式的懒加载
     * @return
     */
    private SqlAuthInject getSqlAuthInject() {
        if (sqlAuthInject ==null){
            synchronized (this){
                if (sqlAuthInject == null) {
                    sqlAuthInject = applicationContext.getBean(SqlAuthInject.class);
                }
            }
        }

        return sqlAuthInject;
    }


    /**
     * 所有的sql都会进入此拦截器，需要只校验AuthRangeContext中有数据的，没有数据的说明不是@AuthRange 所拦截的，应当原样输出
     * AuthRangeContext.get()有数据说明一定是从{@link AuthDataRange}拦截的，并在{@link DataRangeAspect}中加入上下文数据且不为空
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        AuthRange authRange = DataRangeContext.get();
        if (authRange==null){
            log.debug("DataRangeContext 为空，不做范围查询SQL增强处理");
            return invocation.proceed();
        }

        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        if (log.isDebugEnabled()){
            log.debug("原始SQL为:{}", sql);
        }
        //获取新的SQL
        String newSql = getSqlAuthInject().convert2NewSql(sql, authRange);

        //通过反射设置值
        MetaObject metaObject = SystemMetaObject.forObject(boundSql);
        metaObject.setValue("sql",newSql);

        if (log.isDebugEnabled()){
            log.debug("修改之后新的SQL为：{}", newSql);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
        //return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
