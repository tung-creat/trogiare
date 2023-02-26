package com.trogiare.repo;

import com.trogiare.common.enumrate.PostStatusEnum;
import com.trogiare.common.enumrate.PostTypeEnum;
import com.trogiare.common.enumrate.TypeRealEstateEnum;
import com.trogiare.model.Post;
import com.trogiare.model.impl.PostAndAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepo extends PagingAndSortingRepository<Post, String> {
    @Query(value =
            "SELECT p as post,ad as address " +
                    "FROM Post p LEFT JOIN Address ad ON ad.id = p.addressId " +
                    "WHERE p.status = :status " + // added status parameter to query
                    "AND (:address is null OR ad.addressDetails LIKE CONCAT('%', :address, '%')) " +
                    "AND (:priceMin is null OR :priceMin <= p.price) " +
                    "AND (:priceMax is null OR :priceMax >= p.price) " +
                    "AND (:keyword is null OR p.description LIKE CONCAT('%', :keyword, '%')) " +
                    "AND (:areaMin is null OR p.useableArea >= :areaMin) " +
                    "AND (:areaMax is null OR p.useableArea <= :areaMax) " +
                    "AND (:bedRoom is null OR p.bedroom = :bedRoom) " +
                    "AND (:type is null OR p.typePost = :type) " )
//                    "AND (:typeRealEstate is null OR p.typeRealEstate = :typeRealEstate)")
    Page<PostAndAddress> getPosts(Pageable pageable,
                                  @Param("address") String address,
                                  @Param("priceMin") Long priceMin,
                                  @Param("priceMax") Long priceMax,
                                  @Param("keyword") String keyword,
                                  @Param("areaMin") Long areaMin,
                                  @Param("areaMax") Long areaMax,
                                  @Param("bedRoom") Long bedRoom,
                                  @Param("type") PostTypeEnum type,
//                                  @Param("typeRealEstate") TypeRealEstateEnum typeRealEstate,
                                  @Param("status") PostStatusEnum status);


    @Query(value =
            "SELECT p as post,ad as address " +
                    " FROM Post p" +
                    " LEFT JOIN Address ad" +
                    " ON ad.id = p.addressId where p.id = :postId")
    Optional<PostAndAddress> getPostById(@Param("postId") String postId);
    //    addressDetails,Address.province,Address.district,Address.village
}
