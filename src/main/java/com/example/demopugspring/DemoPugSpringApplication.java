package com.example.demopugspring;

import com.example.demopugspring.model.Application;
import com.example.demopugspring.model.Integration;
import com.example.demopugspring.model.Mapper;
import com.example.demopugspring.model.Message;
import com.example.demopugspring.service.ApplicationService;
import com.example.demopugspring.service.IntegrationService;
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



    /*@Bean
    public CommandLineRunner loadApplications(ApplicationService applicationService,
                                              MessageService messageService,
                                              MapperService mapperService,
                                              IntegrationService integrationService) {
        return (args) -> {
            // save apps
            applicationService.save(new Application("GH", "Global Care"));
            applicationService.save(new Application("DOTLOGIC", "Dotlogic"));
            applicationService.save(new Application("CARDIOBASE", "Cardiobase"));
            applicationService.save(new Application("CWM", "Fuji"));

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
            mapperService.save(new Mapper(Arrays.asList("/MSH-9-3"), "OMG_O19", Mapper.Category.TEXT));
            mapperService.save(new Mapper(Arrays.asList("/MSH-12"), "2.5", Mapper.Category.TEXT));
            mapperService.save(new Mapper(Arrays.asList("/ORDER/OBSERVATION(0)/OBX-1"), "1", Mapper.Category.TEXT));
            mapperService.save(new Mapper(Arrays.asList("/NTE-3"), "Nota do MSH", Mapper.Category.TEXT));
            mapperService.save(new Mapper(Arrays.asList("/PATIENT/NTE-3"), "Nota do patient", Mapper.Category.TEXT));
            mapperService.save(new Mapper(Arrays.asList("/ORDER/NTE-3"), "Nota da Order", Mapper.Category.TEXT));
            mapperService.save(new Mapper(Arrays.asList("/ORDER/OBSERVATION/NTE-3"), "Nota da Observation", Mapper.Category.TEXT));
            mapperService.save(new Mapper(Arrays.asList("/ORDER/OBR-4-1","/ORDER/OBR-4-2","/ORDER/OBR-4-3"), "ICD-10", Mapper.Category.TRANSCODING));
            mapperService.save(new Mapper(Arrays.asList("/MSH-3"), "/MSH-5", Mapper.Category.SWAP));
            mapperService.save(new Mapper(Arrays.asList("/ORDER/OBSERVATION/OBR-3"), "/ORDER/OBSERVATION/OBR-2", Mapper.Category.FIELD));
            mapperService.save(new Mapper(Arrays.asList("/ORDER/OBSERVATION/OBR-2"), "", Mapper.Category.TEXT));
            mapperService.save(new Mapper(Arrays.asList("/ORDER/ORC"), "", Mapper.Category.SEGMENT));
            mapperService.save(new Mapper(Arrays.asList("/ORDER/ORC-3"), "/MSH-4,/ORDER/ORC-2", Mapper.Category.JOIN));
            mapperService.save(new Mapper(Arrays.asList("/PATIENT/PID/PID-3-1"), "", Mapper.Category.NUMERIC));
            // fetch all mappers
            logger.info("Mappers found with findAll():");
            logger.info("-------------------------------");
            for (Mapper mapper : mapperService.findAll()) {
                logger.info(mapper.toString());
            }
            logger.info("");

            logger.info("");
            integrationService.save(new Integration(messageService.findById(5L), applicationService.findById(1L), applicationService.findById(2L), Arrays.asList(mapperService.findById(8L))));

            // fetch all integrations
            logger.info("Integrations found with findAll():");
            logger.info("-------------------------------");
            for (Integration integration : integrationService.findAll()) {
                logger.info(integration.toString());
            }
            logger.info("");
        };
    }*/



}
