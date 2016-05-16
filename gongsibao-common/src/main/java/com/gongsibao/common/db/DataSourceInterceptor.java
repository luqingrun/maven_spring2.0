package com.gongsibao.common.db;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * 设置数据源KEY的拦截器
 */
public class DataSourceInterceptor implements MethodInterceptor{
    // 数据源key的存储控制器
    private DataSourceKey dataSourceKey;
    @Autowired
    private TransactionInterceptor transactionInterceptor;

    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        TransactionAttributeSource transactionAttributeSource = transactionInterceptor.getTransactionAttributeSource();
        TransactionAttribute transactionAttribute = transactionAttributeSource.getTransactionAttribute(method, method.getDeclaringClass());
        int propagationBehavior = transactionAttribute.getPropagationBehavior();
        if (readSet.contains(propagationBehavior)) {
            dataSourceKey.setReadKey();
        }
        if (writeSet.contains(propagationBehavior)) {
            dataSourceKey.setWriteKey();
        }
        return invocation.proceed();
    }

    public void setDataSourceKey(DataSourceKey dataSourceKey) {
        this.dataSourceKey = dataSourceKey;
    }

    public static Set<Integer> writeSet = new HashSet<Integer>();
    public static Set<Integer> readSet = new HashSet<Integer>();

    static {
        writeSet.add(TransactionDefinition.PROPAGATION_REQUIRED);
        writeSet.add(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        writeSet.add(TransactionDefinition.PROPAGATION_MANDATORY);
        writeSet.add(TransactionDefinition.PROPAGATION_NESTED);

        readSet.add(TransactionDefinition.PROPAGATION_NEVER);
        readSet.add(TransactionDefinition.PROPAGATION_SUPPORTS);
        readSet.add(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
    }

}
