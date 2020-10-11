package com.example.demopugspring.repository;

import com.example.demopugspring.model.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {
}
