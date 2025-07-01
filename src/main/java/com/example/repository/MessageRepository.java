package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

import java.util.Optional;


public interface MessageRepository extends JpaRepository<Message, Long>{

    @Query("FROM Message WHERE messageId = :messageId")
    Optional<Message> findByMessageID(@Param("messageId") Integer messageId);
}
