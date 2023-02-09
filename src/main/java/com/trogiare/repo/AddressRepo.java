package com.trogiare.repo;

import com.trogiare.model.Address;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AddressRepo extends PagingAndSortingRepository<Address,String> {
}
