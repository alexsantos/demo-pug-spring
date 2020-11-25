package com.example.demopugspring.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demopugspring.model.Application;
import com.example.demopugspring.model.Integration;
import com.example.demopugspring.model.Message;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegrationRepository extends CrudRepository<Integration, Long> {
	Integration findByMessageAndSendingAppAndReceivingApp(Message message, Application sending, Application receiving);

}
