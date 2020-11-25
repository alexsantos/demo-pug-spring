package com.example.demopugspring.repository;

import com.example.demopugspring.model.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
	Message findByCodeAndEvent(String code, String event);

	Message findByCodeAndEventAndVersion(String code, String event, Message.Version version);

	List<Message> findByOrderByCodeAscEventAscVersionAsc();
}
