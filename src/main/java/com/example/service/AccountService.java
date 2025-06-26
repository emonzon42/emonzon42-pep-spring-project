package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.repository.AccountRepository;

public class AccountService {

    AccountRepository repo;

    @Autowired
    public AccountService(AccountRepository repository){
        this.repo = repository;
    }
    

}
