package com.demo.account.accountapp.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transaction")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "source_account_id")
    private Long debitedAccountId;

    @Column(name = "destination_account_id")
    private Long creditedAccountId;

    @Column(name = "message")
    private String message;

    @Column(name = "created_on")
    private LocalDateTime createdTime;

}
