package com.trogiare.repo;

import com.trogiare.model.ObjectMedia;

import com.trogiare.model.impl.PostIddAndPathImages;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectMediaRepo extends PagingAndSortingRepository<ObjectMedia,String>, ListCrudRepository<ObjectMedia,String> {
    @Query(value="select ob.objectId as postId,ob.refType as typeImage,f.path as path" +
            " from ObjectMedia ob left join FileSystem f " +
            " on ob.mediaId = f.id where ob.objectId in :listPostId " +
            " and (:refType is null or ob.refType = :refType)")
    List<PostIddAndPathImages> getImagesByPostIds(@Param("listPostId") List<String> postIds,
                                                  @Param("refType") String refType);
}
