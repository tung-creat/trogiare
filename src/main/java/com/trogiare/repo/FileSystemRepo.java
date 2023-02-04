package com.trogiare.repo;

import com.trogiare.model.FileSystem;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface  FileSystemRepo extends PagingAndSortingRepository<FileSystem,String>, ListCrudRepository<FileSystem,String> {
    Optional<FileSystem> findByPath(String path);
}
