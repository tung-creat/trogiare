package com.trogiare.repo;

import com.trogiare.model.ConverStation;
import com.trogiare.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConverStationRepo extends MongoRepository<ConverStation,String> {

    ConverStation findByParticipants(List<String> participants);
    Page<ConverStation> findByParticipantsContaining(String uid,Pageable pageable);

}
