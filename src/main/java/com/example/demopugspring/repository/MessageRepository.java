package com.example.demopugspring.repository;

import com.example.demopugspring.model.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {
	public Message findByCodeAndEvent(String code, String event);

	public Message findByCodeAndEventAndVersion(String code, String event, Message.Version version);

	public List<Message> findByOrderByCodeAscEventAscVersionAsc();
}
