package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Message;

import java.util.Optional;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{

    @Query("FROM Message WHERE messageId = :messageId")
    Optional<Message> findByMessageID(@Param("messageId") Integer messageId);

    @Query("FROM Message WHERE postedBy = :postedBy")
    List<Message> findAllByPosterID(@Param("postedBy") Integer postedBy);

    @Modifying 
    @Transactional
    @Query("DELETE FROM Message WHERE messageId = :messageId")
    void deleteByMessageID(@Param("messageId") Integer messageId);

    @Modifying 
    @Transactional
    @Query("UPDATE Message SET messageText = :messageText WHERE messageId = :messageId")
    void updateMessage(@Param("messageId") Integer messageId, @Param("messageText") String messageText);
    


}
