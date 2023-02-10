package com.trogiare.repo;

import com.trogiare.model.News;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsRepo extends PagingAndSortingRepository<News,String> {

    Optional<News> findByAuthorIdAndId(String userId,String newsId);
}
