package com.trogiare.repo;

import com.trogiare.common.enumrate.PostTypeEnum;
import com.trogiare.model.Post;
import com.trogiare.model.User;
import com.trogiare.model.impl.PostAndAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface PostRepo extends PagingAndSortingRepository<Post, String> {
    @Query(value =
            "SELECT p as post,ad as address " +
                    "FROM Post p LEFT JOIN Address ad ON ad.id = p.addressId " +
                    "where p.status like 'PUBLIC'" +
                    "and (:address is null or ad.addressDetails like concat('%',:address,'%')) " +
                    "and (:priceMin is null or :priceMin <= p.price)" +
                    "and (:priceMax is null or :priceMax >= p.price)" +
                    "and (:keyword is null or p.description like concat('%',:keyword,'%'))" +
                    "and (:areaMin is null or p.useableArea >= :areaMin)" +
                    "and (:areaMax is null or p.useableArea <= :areaMax)" +
                    "and (:bedRoom is null or p.bedroom = :bedRoom)" +
                    "and (:type is null or p.typePost = :type)")
    Page<PostAndAddress> getPosts(Pageable pageable,
                                  @Param("address") String address,
                                  @Param("priceMin") Long priceMin,
                                  @Param("priceMax") Long priceMax,
                                  @Param("keyword") String keyword,
                                  @Param("areaMin") Long areaMin,
                                  @Param("areaMax") Long areaMax,
                                  @Param("bedRoom") Long bedRoom,
                                  @Param("type") PostTypeEnum type);

    @Query(value =
            "SELECT p as post,ad as address " +
                    " FROM Post p" +
                    " LEFT JOIN Address ad" +
                    " ON ad.id = p.addressId where p.id = :postId and p.status not like 'DELETED'")
    Optional<PostAndAddress> getPostById(@Param("postId") String postId);
    //    addressDetails,Address.province,Address.district,Address.village
}
