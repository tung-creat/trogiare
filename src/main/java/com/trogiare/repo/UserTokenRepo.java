package com.trogiare.repo;

import com.trogiare.model.UserRole;
import com.trogiare.model.UserToken;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserTokenRepo extends PagingAndSortingRepository<UserToken,String>, ListCrudRepository<UserToken,String> {
}
