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

    private MessageRepository repo;
    private AccountService accountService;

    public final byte MESSAGE_VALID = 1;
    public final byte MESSAGE_IS_BLANK = -1;
    public final byte MESSAGE_TOO_LONG = -2;
    public final byte MESSAGE_POSTER_DOESNT_EXIST = -3;

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
            return MESSAGE_IS_BLANK;
        }
        if (msg.getMessageText().length() > 255 ){
            return MESSAGE_TOO_LONG;
        }
        
        return MESSAGE_VALID;
    }

    /* 
     * validates if message is associated by a real account in db
     * works in tandem with validateMessage
     */
    public byte validateMessagePoster(Message msg){
        if (accountService.verifyAccount(msg.getPostedBy()) == null){
            return MESSAGE_POSTER_DOESNT_EXIST;
        } else {
            return validateMessage(msg);
        }
    }

    
}
