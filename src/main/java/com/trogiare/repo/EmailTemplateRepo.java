package com.trogiare.repo;

import com.trogiare.model.EmailTemplate;
import com.trogiare.model.UserToken;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface EmailTemplateRepo extends PagingAndSortingRepository<EmailTemplate,String> {
    Optional<EmailTemplate> findByCode(String code);
}
