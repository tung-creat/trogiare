package com.trogiare.repo;

import com.trogiare.model.Post;
import com.trogiare.model.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostRepo extends PagingAndSortingRepository<Post,String>, ListCrudRepository<Post,String> {
}
