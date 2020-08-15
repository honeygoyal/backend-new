package com.egatetutor.backend.repository;


import com.egatetutor.backend.model.SubscriptionDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends CrudRepository<SubscriptionDetails, Long> {

	@Override
	Optional<SubscriptionDetails> findById(Long sid);

	@Query(value = "SELECT course_offered_id FROM subscription_details s WHERE s.user_id=:user_id",nativeQuery = true)
	List<Long> findByUserId(@Param("user_id") Long userId);



}
