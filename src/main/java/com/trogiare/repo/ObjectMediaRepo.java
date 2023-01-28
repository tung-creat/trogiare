package com.trogiare.repo;

import com.trogiare.model.ObjectMedia;
import com.trogiare.model.impl.PostIddAndImageName;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectMediaRepo extends PagingAndSortingRepository<ObjectMedia,String>, ListCrudRepository<ObjectMedia,String> {
    @Query(value="select ob.objectId as postId,f.name as imageName " +
            "from ObjectMedia ob  left join FileSystem f " +
            "on ob.mediaId = f.id where ob.objectId in :listPostId " +
            "and (:refType is null or ob.refType = :refType)")
    List<PostIddAndImageName> getImagesByPostIds(@Param("listPostId") List<String> postIds,
                                                 @Param("refType") String refType);
}
