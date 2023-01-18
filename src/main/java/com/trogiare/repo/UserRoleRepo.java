package com.trogiare.repo;


import com.trogiare.model.UserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;


public interface UserRoleRepo extends PagingAndSortingRepository<UserRole,String> , ListCrudRepository<UserRole,String> {
    List<UserRole> findByUserId(String userId);
}
