package org.tus.tx.service.message.persistence;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tus.common.domain.persistence.PersistenceService;
import org.tus.common.domain.persistence.QueryService;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class PersistenceCommonConfiguration {

    @Bean
    public QueryService queryService(EntityManagerFactory entityManagerFactory, DataSource dataSource) {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        return new PersistenceService(sessionFactory, dataSource);
    }
}

