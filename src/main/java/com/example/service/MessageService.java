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
        if (validateNewMessage(msg) != MESSAGE_VALID) {
            return null;
        }
        return repo.save(msg);
    }
    
    public Message findMessage(int messageId){
        Optional<Message> optional = repo.findByMessageID(messageId);

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

    public List<Message> findAllMessagesBy(Integer user){
        return repo.findAllByPosterID(user);
    }

    /*
     * updates a message if it exists in the db 
     * returns '1' if message was valid and was successfully updated
     * '0' if original message wasn't found or valid
     */
    
        public byte editMessage(Integer messageId, String messageText){
        if(findMessage(messageId) != null && validateMessage(messageText) == MESSAGE_VALID){ //message exists in db and meets validation rules
            repo.updateMessage(messageId, messageText);
            return 1; 
        }
        return 0;
    }

    
    /*
     * deletes a message if it exists in the db 
     * returns '1' if message existed and was successfully deleted
     * '0' if message wasn't found
     */
    public byte deleteMessage(int messageId){
        Optional<Message> optional = repo.findByMessageID(messageId);

        if (optional.isPresent()) {
            repo.deleteByMessageID(messageId);
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

    public byte validateMessage(String msgText){
        return validateMessage(new Message(null, msgText, null));
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
