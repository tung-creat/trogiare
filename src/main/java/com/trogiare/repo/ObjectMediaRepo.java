package com.trogiare.repo;

import com.trogiare.common.enumrate.ObjectMediaRefValueEnum;
import com.trogiare.model.ObjectMedia;

import com.trogiare.model.impl.ObjectIddAndPathImages;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
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
    @Query(value="select f.path " +
            " from ObjectMedia ob left join FileSystem f " +
            " on ob.mediaId = f.id where ob.objectId in :listObjectId ")
    List<String> getPathImageFromObjectid(@Param("listObjectId") List<String> listObjectId);
    @Transactional
    @Modifying
    @Query(value="delete o, m " +
            "from object_media as o " +
            "join file_system as m  ON o.media_id = m.id " +
            "WHERE o.object_id in :objectIds", nativeQuery = true)
    void deleteObjectMediaAndMediaByObjectIds(@Param("objectIds")List<String> objectIds);
    @Transactional
    @Modifying
    @Query(value="delete o, m " +
            "from object_media as o " +
            "join file_system as m  ON o.media_id = m.id " +
            "WHERE m.path in :paths", nativeQuery = true)
    void deleteObjectMediaAndMediaByPaths(@Param("paths")List<String> paths);
    @Transactional
    @Modifying
    @Query(value="delete o, m " +
            "from object_media as o " +
            "join file_system as m  ON o.media_id = m.id " +
            "WHERE o.object_id = ? 1 and o.refType ='IMAGE_POST' ", nativeQuery = true)
    void deleteObjectMediaAndMediaAvatarByObjectIds(String idObject);
    @Query(value="select f.path " +
            " from ObjectMedia ob left join FileSystem f " +
            " on ob.mediaId = f.id where ob.objectId = :objectId and (:reftype is null or ob.refType = :reftype)")
    String getPathImageAvatarFromObjectId(@Param("objectId") String objectId,@Param("reftype") String reftype);

    Optional<ObjectMedia> findByObjectId(String objectId);
    Optional<ObjectMedia> findByMediaId(String mediaId);
}
