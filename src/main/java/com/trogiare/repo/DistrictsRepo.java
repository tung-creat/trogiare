package com.trogiare.repo;

import com.trogiare.model.Districts;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DistrictsRepo extends PagingAndSortingRepository<Districts,String> {
    List<Districts> findByProvinceCode(String provincesCode);
}
