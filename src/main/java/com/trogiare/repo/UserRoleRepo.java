package com.trogiare.repo;


import com.trogiare.model.UserRole;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;


public interface UserRoleRepo extends PagingAndSortingRepository<UserRole,String> {
    List<UserRole> findByUserId(String userId);
}
