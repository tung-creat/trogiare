package com.trogiare.repo;

import com.trogiare.model.FileSystem;
import com.trogiare.model.Provinces;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProvincesRepo extends PagingAndSortingRepository<Provinces,String>{
    @Query(value="select pv.fullName,dt.fullName,w.fullName from Provinces as pv " +
            "left join Districts as dt on pv.code = dt.provinceCode " +
            "left join Wards as w on w.districtCode = dt.code " +
            "where pv.code = :provinceId and dt.code = :districtId and w.code = :villageId")
    List<Object[]> getDetailAddress(@Param("provinceId") String provinceId,
                              @Param("districtId") String districtId,
                              @Param("villageId") String villageId);
}
