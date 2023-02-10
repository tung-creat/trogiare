package com.trogiare.repo;

import com.trogiare.model.News;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepo extends PagingAndSortingRepository<News,String> {

    Optional<News> findByAuthorIdAndId(String userId,String newsId);
    @Query(value ="select n from News n " +
            "where n.statusNews ='PUBLIC'"+
            "and (:keyword is null or n.title like concat('%',:keyword ,'%'))" +
            "and (:timeStart is null or n.createdTime >= :timeStart) " +
            "and (:timeEnd is null or n.createdTime <= :timeEnd)" +
            "and (:topic is null or n.topic = :topic)")
    Page<News> getAllNewsByParams(Pageable page,
                            @Param("keyword") String keyword,
                            @Param("timeStart")LocalDate timeStart,
                            @Param("timeEnd") LocalDate timeEnd,
                            @Param("topic") String topic);


}
