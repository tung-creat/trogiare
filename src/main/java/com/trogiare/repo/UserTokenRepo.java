package com.trogiare.repo;

import com.trogiare.model.UserRole;
import com.trogiare.model.UserToken;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserTokenRepo extends PagingAndSortingRepository<UserToken,String> {
    Optional<UserToken> findByToken(String token);
    Optional<UserToken> findByTokenAndUserId(String token,String userId);
    @Query("SELECT u FROM UserToken u WHERE u.userId = ?1 and u.tokenType=?2 order by u.createdTime desc")
    List<UserToken> findTokenRequested(String userId, String type, Pageable pageable);
}
