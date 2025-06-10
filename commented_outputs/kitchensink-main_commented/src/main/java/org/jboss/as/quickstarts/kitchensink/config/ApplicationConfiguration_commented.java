package org.jboss.as.quickstarts.kitchensink.config;

import org.jboss.as.quickstarts.kitchensink.model.DatabaseSequence;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
/**
 * Configures application settings and initializes necessary components upon application readiness.
 * This class listens for the ApplicationReadyEvent to perform setup tasks related to MongoDB operations.
 */

public class ApplicationConfiguration implements ApplicationListener<ApplicationReadyEvent> {

    private final MongoOperations mongoOperations;

    @Autowired
    public ApplicationConfiguration(final MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
/**
 * Initializes necessary collections on application startup.
 *
 * @param event Application ready event
 */

    public void onApplicationEvent(final ApplicationReadyEvent event) {
        if (!mongoOperations.collectionExists(DatabaseSequence.class)) {
            mongoOperations.createCollection(DatabaseSequence.class);
        }
        if (!mongoOperations.collectionExists(Member.class)) {
            mongoOperations.createCollection(Member.class);
        }
    }

    @Bean
/**
 * Creates a ValidatingMongoEventListener instance.
 *
 * @param factory LocalValidatorFactoryBean for validation
 * @return ValidatingMongoEventListener instance
 */

    public ValidatingMongoEventListener validatingMongoEventListener(final LocalValidatorFactoryBean factory) {
        return new ValidatingMongoEventListener(factory);
    }

    @Bean
/**
 * Creates a new instance of LocalValidatorFactoryBean.
 *
 * @return Instance of LocalValidatorFactoryBean
 */

    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}
