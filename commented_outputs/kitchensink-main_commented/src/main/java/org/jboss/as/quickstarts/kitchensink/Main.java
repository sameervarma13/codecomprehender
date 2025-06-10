package org.jboss.as.quickstarts.kitchensink;

import org.jboss.as.quickstarts.kitchensink.config.ApplicationConfiguration;
import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@Import(value = MongoAutoConfiguration.class)
@EnableMongoRepositories(basePackageClasses = MemberRepository.class)
public class Main extends SpringBootServletInitializer {
    public static void main(String[] args) {
        try {
            SpringApplication.run(ApplicationConfiguration.class, args);
        } catch (Exception e) {
            LoggerFactory.getLogger(Main.class).error(e.getStackTrace().toString(), e);
        }
    }
}
