package com.trogiare.repo;

import com.trogiare.model.Address;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AddressRepo extends PagingAndSortingRepository<Address, String> {
    @Modifying
    void deleteById(String addressId);
}
