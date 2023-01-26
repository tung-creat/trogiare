package com.trogiare.repo;

import com.trogiare.model.FileSystem;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FileSystemRepo extends PagingAndSortingRepository<FileSystem,String>, ListCrudRepository<FileSystem,String> {
}
