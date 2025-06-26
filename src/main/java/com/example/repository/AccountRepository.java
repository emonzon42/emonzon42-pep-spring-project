package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

import com.example.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{

    @Query("FROM Account WHERE username = :username")
    Account findByUsername(@Param("username") String username);

    @Query("FROM Account WHERE username = :username AND password = :password")
    Optional<Account> findByAccountDetails(@Param("username") String username, @Param("password") String password);
}
