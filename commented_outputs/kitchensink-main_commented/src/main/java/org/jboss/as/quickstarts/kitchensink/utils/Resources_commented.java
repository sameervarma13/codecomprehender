package org.jboss.as.quickstarts.kitchensink.utils;

import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.logging.Logger;

@Configuration
/** 
 * The Resources class provides a Spring configuration for producing prototype-scoped Logger instances. 
 * It utilizes the InjectionPoint to create loggers specific to the declaring class of the injection. 
 */

public class Resources {

    @Bean
    @Scope("prototype")
/**
 * Produces a logger for a given injection point.
 *
 * @param injectionPoint Injection point for logger
 * @return Logger instance for the class
 */

    public Logger produceLogger(InjectionPoint injectionPoint) {
        Class<?> classOnWired = injectionPoint.getMember().getDeclaringClass();
        return Logger.getLogger(classOnWired.getName());
    }
}
