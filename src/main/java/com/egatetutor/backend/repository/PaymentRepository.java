package com.egatetutor.backend.repository;

import com.egatetutor.backend.model.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long>
{

}
