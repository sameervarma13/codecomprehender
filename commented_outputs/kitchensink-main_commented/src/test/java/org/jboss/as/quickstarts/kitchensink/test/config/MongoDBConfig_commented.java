package org.jboss.as.quickstarts.kitchensink.test.config;

import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

@Configuration
public class MongoDBConfig {
    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest").withExposedPorts(27017);

    static {
        mongoDBContainer.start();
        Integer port = mongoDBContainer.getMappedPort(27017);
        System.setProperty("mongodb.container.port", String.valueOf(port));
    }
}
