package ru.buisnesslogiclab1.config;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;


@Configuration
public class JtaConfig {

    @Bean(name = "bitronixTransactionManager")
    public BitronixTransactionManager bitronixTransactionManager() throws Throwable {
        BitronixTransactionManager bitronixTransactionManager = TransactionManagerServices.getTransactionManager();
        bitronixTransactionManager.setTransactionTimeout(10000);
        return bitronixTransactionManager;
    }

}

