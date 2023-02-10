package com.trogiare.repo;

import com.trogiare.common.enumrate.ObjectMediaRefValueEnum;
import com.trogiare.model.ObjectMedia;

import com.trogiare.model.impl.ObjectIddAndPathImages;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObjectMediaRepo extends PagingAndSortingRepository<ObjectMedia,String> {
    @Query(value="select ob.objectId as objectId,ob.refType as typeImage,f.path as path" +
            " from ObjectMedia ob left join FileSystem f " +
            " on ob.mediaId = f.id where ob.objectId in :listObjectId " +
            " and (:refType is null or ob.refType = :refType)")
    List<ObjectIddAndPathImages> getImagesByObjectIds(@Param("listObjectId") List<String> objectId,
                                                  @Param("refType") String refType);


    Optional<ObjectMedia> findByObjectId(String objectId);
    Optional<ObjectMedia> findByMediaId(String mediaId);
}
