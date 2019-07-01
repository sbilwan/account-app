package com.demo.account.accountapp.repository;

import com.demo.account.accountapp.dao.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query(value = " select * from transaction where source_account_id = :accountid " +
            "or destination_account_id = :accountid", nativeQuery = true)
    List<TransactionEntity> findAccountTransactionByAccountId(@Param("accountid") final Long accountId);
}
