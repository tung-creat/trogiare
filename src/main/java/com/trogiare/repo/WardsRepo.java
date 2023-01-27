package com.trogiare.repo;

import com.trogiare.model.FileSystem;
import com.trogiare.model.Wards;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface WardsRepo extends PagingAndSortingRepository<Wards,String>, ListCrudRepository<Wards,String> {
    List<Wards> findByDistrictCode(String districtsCode);
}
