package com.trogiare.repo;

import com.trogiare.model.Notification;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NotificationRepo extends PagingAndSortingRepository<Notification,String> {

}
