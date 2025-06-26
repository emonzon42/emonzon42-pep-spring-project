package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.repository.MessageRepository;

public class MessageService {

    MessageRepository repo;

    @Autowired
    public MessageService(MessageRepository repository){
        this.repo = repository;
    }
}
