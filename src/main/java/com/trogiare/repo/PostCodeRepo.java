package com.trogiare.repo;

import com.trogiare.model.PostCode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCodeRepo extends PagingAndSortingRepository<PostCode,Long>,ListCrudRepository<PostCode,Long> {
    @Query(value ="SELECT MAX(p.numberCode) FROM PostCode as p")
    Long getRecordPostCode();
}
