package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.example.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{

    @Query("FROM Account WHERE username = :username")
    Account findByUsername(@Param("username") String username);

    @Query("FROM Account WHERE username = :username AND password = :password")
    Optional<Account> findByAccountDetails(@Param("username") String username, @Param("password") String password);

    @Query("FROM Account WHERE accountId = :accountId")
    Optional<Account> findByAccountID(@Param("accountId") Integer accountId);
}
