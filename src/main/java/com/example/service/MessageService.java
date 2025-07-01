package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import com.example.service.AccountService;

@Service
public class MessageService {

    public MessageRepository repo;
    public AccountService accountService;

    @Autowired
    public MessageService(MessageRepository repository, AccountService accountService){
        this.repo = repository;
        this.accountService = accountService;
    }

    public Message createMessage(Message msg){
        if (validateNewMessage(msg) != 1) {
            return null;
        }
        return repo.save(msg);
    }
    
    public Message findMessage(int message_id){
        Optional<Message> optional = repo.findById((long) message_id);

        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

    public Message findMessage(Message msg){
        return findMessage(msg.getMessageId());
    }

    /*
     * deletes a message if it exists in the db 
     * returns '1' if message existed and was successfully deleted
     * '0' if message wasn't found
     */
    public byte deleteMessage(int message_id){
        Optional<Message> optional = repo.findById((long) message_id);

        if (optional.isPresent()) {
            repo.deleteById((long) message_id);
            return 1;
        } else {
            return 0;
        }
    }

    /*
     * validates a new message in db by returning a '1'
     * if any of these invalidation conditions are met then it will return the corresponding number:
     * -1: message is blank
     * -2: message is greater than 255 characters
     * -3: posted_by doesn't refers to real user in db
     */
    public byte validateNewMessage(Message msg){
        if (msg.getMessageText().isBlank()){
            return -1;
        }
        if (msg.getMessageText().length() > 255 ){
            return -2;
        }
        if (accountService.verifyAccount(msg.getPostedBy()) == null){
            return -3;
        }
        return 1;
    }
    
}
