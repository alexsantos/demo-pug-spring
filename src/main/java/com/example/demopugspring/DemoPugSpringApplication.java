package com.example.demopugspring;

import com.example.demopugspring.model.Application;
import com.example.demopugspring.model.Mapper;
import com.example.demopugspring.model.Message;
import com.example.demopugspring.service.ApplicationService;
import com.example.demopugspring.service.MapperService;
import com.example.demopugspring.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class DemoPugSpringApplication {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        SpringApplication.run(DemoPugSpringApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadApplications(ApplicationService applicationService,
                                              MessageService messageService,
                                              MapperService mapperService) {
        return (args) -> {
            // save apps
            applicationService.save(new Application("GH", "Global Care"));
            applicationService.save(new Application("DOTLOGIC", "Dotlogic"));
            applicationService.save(new Application("CARDIOBASE", "Cardiobase"));
            applicationService.save(new Application("CWM", "Fuji"));
            applicationService.save(new Application("CWM1", "Fuji"));
            applicationService.save(new Application("CWM2", "Fuji"));
            applicationService.save(new Application("CWM3", "Fuji"));
            applicationService.save(new Application("CWM4", "Fuji"));
            applicationService.save(new Application("CWM5", "Fuji"));
            applicationService.save(new Application("CWM6", "Fuji"));
            applicationService.save(new Application("CWM7", "Fuji"));

            // fetch all apps
            logger.info("Applications found with findAll():");
            logger.info("-------------------------------");
            for (Application application : applicationService.findAll()) {
                logger.info(application.toString());
            }
            logger.info("");

            messageService.save(new Message("OMG", "O19"));
            messageService.save(new Message("ORU", "R01"));
            messageService.save(new Message("ADT", "A08"));

            // fetch all messages
            logger.info("Messages found with findAll():");
            logger.info("-------------------------------");
            for (Message message : messageService.findAll()) {
                logger.info(message.toString());
            }
            logger.info("");
            String[] keys = {"/MSH-9-3"};
            Mapper map = new Mapper(Arrays.asList(keys.clone()), "OMG_O19", Mapper.Category.TEXT);
            logger.info(map.toString());
            mapperService.save(map);

            // fetch all messages
            logger.info("Mappers found with findAll():");
            logger.info("-------------------------------");
            for (Mapper mapper : mapperService.findAll()) {
                logger.info(mapper.toString());
            }
            logger.info("");
        };
    }

}
