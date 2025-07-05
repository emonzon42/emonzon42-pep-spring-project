package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired AccountService as;
    @Autowired MessageService ms;

    @PostMapping("register")
    public ResponseEntity register(@RequestBody Account acc){
        boolean nameExists = as.validateNewAccount(acc) == -1; //username exists
        boolean notValid = as.validateNewAccount(acc) < -1; //username is blank or password less than 4
        
        if(nameExists) { 
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else if(notValid){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else{ //account successfully was created in db
            Account savedAcc = as.createAccount(acc);
            return ResponseEntity.status(HttpStatus.OK).body(savedAcc);
        }
    }

    @PostMapping("login") 
    public ResponseEntity login(@RequestBody Account acc){
        Account verifiedAcc = as.verifyAccount(acc);
        if (verifiedAcc != null) { //account details exist in db
            return ResponseEntity.status(HttpStatus.OK).body(verifiedAcc);
        } else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
    }

    @PostMapping("messages")
    public ResponseEntity create(@RequestBody Message msg){
        if (ms.validateNewMessage(msg) == ms.MESSAGE_VALID) {//message successfully validated and will be created
            return ResponseEntity.status(HttpStatus.OK).body(ms.createMessage(msg));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("messages")
    public ResponseEntity getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(ms.findAllMessages());
    }

    @GetMapping("messages/{messageId}")
    public ResponseEntity getMessageByID(@PathVariable Integer messageId){
        return ResponseEntity.status(HttpStatus.OK).body(ms.findMessage(messageId));
    }

    @PatchMapping("messages/{messageId}")
    public ResponseEntity patch(@PathVariable Integer messageId, @RequestBody JsonNode json){
        String messageText = json.path("messageText").asText();
        byte result = ms.editMessage(messageId, messageText);

        if (result > 0) {//message succesfully found and deleted
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("messages/{messageId}")
    public ResponseEntity delete(@PathVariable Integer messageId){
        byte result = ms.deleteMessage(messageId);
        
        if (result > 0) {//message succesfully found and deleted
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }


}
