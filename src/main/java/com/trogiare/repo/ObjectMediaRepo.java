package com.trogiare.repo;

import com.trogiare.model.FileSystem;
import com.trogiare.model.ObjectMedia;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectMediaRepo extends PagingAndSortingRepository<ObjectMedia,String>, ListCrudRepository<ObjectMedia,String> {
}
