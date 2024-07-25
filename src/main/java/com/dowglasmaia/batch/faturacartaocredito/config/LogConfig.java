package com.dowglasmaia.batch.faturacartaocredito.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.FileAppender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class LogConfig {

    @Value("${spring.application.name}")
    private String applicationName;


    @Bean
    public Logger logbackLogger(){
        LoggerContext loggerContext = (LoggerContext) org.slf4j.LoggerFactory.getILoggerFactory();

        FileAppender fileAppender = new FileAppender();
        fileAppender.setContext(loggerContext);

        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String logFileName = String.format("logs/%s-%s.log", date, applicationName);

        fileAppender.setFile(logFileName);


        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}] %m%n");
        encoder.start();

        fileAppender.setEncoder(encoder);
        fileAppender.start();

        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(fileAppender);

        return rootLogger;
    }

}
