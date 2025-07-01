package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    AccountRepository repo;

    @Autowired
    public AccountService(AccountRepository repository){
        this.repo = repository;
    }

    public Account createAccount(Account acc){
        if (validateNewAccount(acc) == 1) {
            return repo.save(acc); 
        } else {
            return null;
        }    
    }

    public Account verifyAccount(Account acc){
        Optional<Account> optional = repo.findByAccountDetails(acc.getUsername(), acc.getPassword());

        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

    public Account verifyAccount(int account_id){
        Optional<Account> optional = repo.findByAccountID(account_id);
        
        if(optional.isPresent()){
            return optional.get();
        }else{
            return null;
        }
    }

    public Account verifyAccount(String username){
        return repo.findByUsername(username);
    }

    public List<Account> getAllAccounts() {
        return repo.findAll();
    }

    /*
     * validates account as long it follows the following rules:
     * username doesn't already exist
     * username not blank
     * password at least 4 characters
     */
    public byte validateNewAccount(Account acc){
        if(verifyAccount(acc.getUsername()) != null ){ return -1;}
        if (acc.getUsername().isBlank() ){ return -2; }
        if( acc.getPassword().length() < 4){ return -3;}
        return 1;
    }
}
