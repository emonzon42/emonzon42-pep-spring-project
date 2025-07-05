package com.example.service;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        Optional<Message> optional = repo.findByMessageID(message_id);

        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

    public Message findMessage(Message msg){
        return findMessage(msg.getMessageId());
    }

    public List<Message> findAllMessages(){
        return repo.findAll();
    }

    public Message editMessage(Message msg){
        if(findMessage(msg) != null && validateMessage(msg) == 1){ //message exists in db and meets validation rules
            return repo.updateMessage(msg.getMessageId(), msg.getMessageText());
        }
        return null;
    }

    
    /*
     * deletes a message if it exists in the db 
     * returns '1' if message existed and was successfully deleted
     * '0' if message wasn't found
     */
    public byte deleteMessage(int message_id){
        Optional<Message> optional = repo.findByMessageID(message_id);

        if (optional.isPresent()) {
            repo.deleteByMessageID(message_id);
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
        return validateMessagePoster(msg);
    }

    /*
     * validates that message meets rules for posting/db storage
     */
    public byte validateMessage(Message msg){
        if (msg.getMessageText().isBlank()){
            return -1;
        }
        if (msg.getMessageText().length() > 255 ){
            return -2;
        }
        
        return 1;
    }

    /* 
     * validates if message is associated by a real account in db
     * works in tandem with validateMessage
     */
    public byte validateMessagePoster(Message msg){
        if (accountService.verifyAccount(msg.getPostedBy()) == null){
            return -3;
        } else {
            return validateMessage(msg);
        }
    }

    
}
