package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.repository.MessageRepository;

@Service
public class MessageService {

    MessageRepository repo;

    @Autowired
    public MessageService(MessageRepository repository){
        this.repo = repository;
    }
}
