package com.example.demopugspring.service;

import com.example.demopugspring.model.Message;
import com.example.demopugspring.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public List<Message> findAll() {
        List<Message> messages = new ArrayList<>();
        messageRepository.findAll().forEach(messages::add);
        return messages;
    }

    public Message findById(Long id) {
        return messageRepository.findById(id).orElseThrow();
    }

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public Message findByCodeAndEvent(String code, String event) {
        return messageRepository.findByCodeAndEvent(code, event);
    }
}
