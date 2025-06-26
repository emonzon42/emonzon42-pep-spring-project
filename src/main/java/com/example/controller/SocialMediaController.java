package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

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
        boolean nameExists = as.verifyAccount(acc.getUsername()) != null; //username exists
        boolean notValid = acc.getUsername().isBlank() || acc.getPassword().length() < 4; //username is blank or password less than 4
        
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
}
