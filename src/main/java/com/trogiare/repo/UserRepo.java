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
    Optional<User> findByUserNameFirt(String name);
    @Query(value="Select * from user u where u.email = ?1 limit 1",nativeQuery = true)
    Optional<User> findByEmailFirt(String email);
    @Query(value="Select * from user u where u.sdt = ?1 limit 1",nativeQuery = true)
    Optional<User> findBySdtFirt(String sdt);
    @Query(value ="select u from User u where u.userName= ?1 or u.email = ?1 ")
    Optional<User> findByUsernameOrEmail(String username);

}
