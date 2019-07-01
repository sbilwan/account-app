package com.demo.account.accountapp.repository;

import com.demo.account.accountapp.dao.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    @Query(value = "Select * from account acc where customer_id = :clientid", nativeQuery = true)
    List<AccountEntity> findByClientId(@Param("clientid") Long clientId);

}
