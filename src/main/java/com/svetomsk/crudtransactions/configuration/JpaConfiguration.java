package com.svetomsk.crudtransactions.configuration;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.svetomsk.crudtransactions.repository")
public class JpaConfiguration {
}
