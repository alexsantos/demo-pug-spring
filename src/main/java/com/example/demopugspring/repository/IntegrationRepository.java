package com.example.demopugspring.repository;

import com.example.demopugspring.model.Application;
import com.example.demopugspring.model.Integration;
import com.example.demopugspring.model.Message;
import org.springframework.data.repository.CrudRepository;

public interface IntegrationRepository extends CrudRepository<Integration, Long> {
    public Integration findByMessageAndSendingAppAndReceivingApp(Message message, Application sending, Application receiving);
}
