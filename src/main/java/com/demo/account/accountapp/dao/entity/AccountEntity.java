package com.demo.account.accountapp.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="account")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "balance")
    private  BigDecimal balance;

    @Column(name = "acc_status")
    private String accountStatus;

    @Column(name = "acc_type")
    private String accountType;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @EqualsAndHashCode.Exclude
    private ClientEntity client;

    @Column(name = "created_on")
    private LocalDateTime createdTime;

    @Column(name = "last_updated_on")
    private LocalDateTime updateTime;

    @Column(name = "overdraw_limit")
    private BigDecimal overDrawLimit;

}
