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
        if(verifyAccount(acc.getUsername()) != null || acc.getUsername().isBlank() || acc.getPassword().length() < 4){
            return null;
        } else{
            return repo.save(acc);
        }     
    }

    public Account verifyAccount(Account acc){
        return verifyAccount(acc.getAccountId());
    }

    public Account verifyAccount(int account_id){
        Optional<Account> optional = repo.findById(Integer.toUnsignedLong(account_id));
        
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

}
