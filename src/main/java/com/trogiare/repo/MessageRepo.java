package com.trogiare.repo;

import com.trogiare.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public interface MessageRepo extends MongoRepository<Message,String> {


    @Query("{ $or : [ { uidSender : ?0, uidRecipient : ?1 }, { uidSender : ?1, uidRecipient : ?0 } ] }")
    Page<Message> findLatestMessages(String uidSender, String uidRecipient, Pageable pageable);
    List<Message> findAllByIdInOrderByTimestampDesc(List<String> messageIds);


}
