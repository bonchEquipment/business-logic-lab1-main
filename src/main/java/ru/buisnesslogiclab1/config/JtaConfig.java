package ru.buisnesslogiclab1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import bitronix.tm.TransactionManagerServices;
import bitronix.tm.jndi.BitronixContext;
import bitronix.tm.resource.jdbc.PoolingDataSource;

import javax.naming.NamingException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

@Configuration
@EnableTransactionManagement
public class JtaConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean(initMethod = "init", destroyMethod = "close")
    public PoolingDataSource dataSource() {
        PoolingDataSource dataSource = new PoolingDataSource();
        dataSource.setClassName("org.postgresql.xa.PGXADataSource");
        //dataSource.setClassName("org.postgresql.Driver");
        dataSource.setUniqueName("jdbc/myPostgresDataSource");
        dataSource.setMinPoolSize(5);
        dataSource.setMaxPoolSize(20);
        dataSource.getDriverProperties().put("user", userName);
        dataSource.getDriverProperties().put("password", password);
        dataSource.getDriverProperties().put("url", url);
        return dataSource;
    }

    @Bean
    public JtaTransactionManager transactionManager() throws NamingException {
        UserTransaction userTransaction = TransactionManagerServices.getTransactionManager();
        TransactionManager transactionManager = TransactionManagerServices.getTransactionManager();
        return new JtaTransactionManager(userTransaction, transactionManager);
    }

    @Bean
    public BitronixContext bitronixContext() {
        return new BitronixContext();
    }
}

