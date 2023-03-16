package com.trogiare.repo;
import com.trogiare.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepo extends PagingAndSortingRepository<User,String> {

    @Query(value ="select u from User u where u.username= ?1 or u.email = ?1 ")
    Optional<User> findByUsernameOrEmail(String username);
    @Query(value ="select u from User u " +
            "where u.username =:username " +
            " or u.sdt = :sdt " +
            " or u.email = :email "
            )
    Optional<User> getUserExists(@Param("username") String username,
                                 @Param("sdt") String sdt,
                                 @Param("email") String email);
    @Query(value = "select u.id,u.firstName,u.lastName,f.path from User as u left join ObjectMedia as o on u.id = o.objectId " +
                   "left join FileSystem f on o.mediaId = f.id " +
                   "where u.id in ?1 ")
    List<Object[]> getInformationUserByListUid(List<String> uids);

}
