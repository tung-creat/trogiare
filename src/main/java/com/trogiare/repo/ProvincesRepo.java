package com.trogiare.repo;

import com.trogiare.model.FileSystem;
import com.trogiare.model.Provinces;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProvincesRepo extends PagingAndSortingRepository<Provinces,String>{
}
