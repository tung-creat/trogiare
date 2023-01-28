package com.trogiare.repo;

import com.trogiare.model.Post;
import com.trogiare.model.User;
import com.trogiare.model.impl.PostAndAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PostRepo extends PagingAndSortingRepository<Post, String>, ListCrudRepository<Post, String> {
    @Query(value =
            "SELECT p as post,ad as address " +
            " FROM Post p" +
            " LEFT JOIN Address ad" +
            " ON ad.id = p.addressId")
    List<PostAndAddress> getPosts(Pageable pageable);
//    addressDetails,Address.province,Address.district,Address.village
}
