package com.trogiare.repo;


import com.trogiare.model.UserRole;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface UserRoleRepo extends PagingAndSortingRepository<UserRole,String> , ListCrudRepository<UserRole,String> {
}
