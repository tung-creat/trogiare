package com.trogiare.repo;

import com.trogiare.model.FileSystem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Repository
public interface  FileSystemRepo extends PagingAndSortingRepository<FileSystem,String> {
    Optional<FileSystem> findByPath(String path);

    Boolean existsByPath(String path);
}
