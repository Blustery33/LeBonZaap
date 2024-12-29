package org.example.back.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {

    private final DataSource dataSource;
    private final Environment environment;

    public LiquibaseConfig(DataSource dataSource, Environment environment) {
        this.dataSource = dataSource;
        this.environment = environment;
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);

        // Spécifie le fichier changelog principal
        liquibase.setChangeLog("classpath:db/liquibase/master.xml");

        // Configure liquibase selon le profil actif
        if (environment.acceptsProfiles(Profiles.of("dev"))
        || environment.acceptsProfiles(Profiles.of("test"))) {
            liquibase.setShouldRun(true); // Liquibase fonctionne en production
        } else {
            liquibase.setShouldRun(false); // Désactive Liquibase en test
        }

        liquibase.setContexts("dev,test"); // Applique uniquement certains contextes
        liquibase.setDropFirst(false); // Empêche de supprimer les tables avant d'exécuter Liquibase

        return liquibase;
    }
}
