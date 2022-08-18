package com.example.as400connector.config;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
@EnableJpaRepositories(transactionManagerRef = AS400Config.DB_CONFIG_PSDB2_TRANSACTION_MANAGER,
    entityManagerFactoryRef = AS400Config.DB_CONFIG_PSDB2_ENTITY_MANAGER_FACTORY,
    basePackages = {"com.example.as400connector.repository"})
public class AS400Config {

  public static final String DB_CONFIG_PSDB2_ENTITY_MANAGER_FACTORY = "psdb2EntityManagerFactory";
  public static final String DB_CONFIG_PSDB2_TRANSACTION_MANAGER = "psdb2TransactionManager";

  @Bean
  @ConfigurationProperties("spring.datasource.psdb2")
  public DataSourceProperties dataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @ConfigurationProperties("spring.datasource.psdb2")
  public DataSource dataSource() {
    return dataSourceProperties().initializeDataSourceBuilder().build();
  }

  @Bean(name = DB_CONFIG_PSDB2_ENTITY_MANAGER_FACTORY)
  public LocalContainerEntityManagerFactoryBean psdb2EntityManagerFactory(
      EntityManagerFactoryBuilder builder) {
    Properties properties = new Properties();
    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.DB2400Dialect");

    LocalContainerEntityManagerFactoryBean emf = builder
        .dataSource(dataSource())
        .packages("com.example.as400connector.domain")
        .persistenceUnit("as400connector")
        .build();
    emf.setJpaProperties(properties);
    return emf;
  }

  @Bean(name = DB_CONFIG_PSDB2_TRANSACTION_MANAGER)
  public JpaTransactionManager psdb2TransactionManager(
      @Qualifier(DB_CONFIG_PSDB2_ENTITY_MANAGER_FACTORY) final EntityManagerFactory emf) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(emf);
    return transactionManager;
  }
}
