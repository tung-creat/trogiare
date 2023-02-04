package com.trogiare.repo;

import com.trogiare.model.News;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepo extends PagingAndSortingRepository<News,String>, ListCrudRepository<News,String> {

}
