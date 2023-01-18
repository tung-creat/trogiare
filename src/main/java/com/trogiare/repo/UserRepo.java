package com.trogiare.repo;
import com.trogiare.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepo extends PagingAndSortingRepository<User,String>, ListCrudRepository<User,String> {
    @Query(value="Select * from user u where u.username = ?1 limit 1",nativeQuery = true)
    Optional<User> findByUserName(String name);
    @Query(value="Select * from user u where u.email = ?1 limit 1",nativeQuery = true)
    Optional<User> findByEmail(String email);
    @Query(value="Select * from user u where u.sdt = ?1 limit 1",nativeQuery = true)
    Optional<User> findBySdt(String sdt);
}
