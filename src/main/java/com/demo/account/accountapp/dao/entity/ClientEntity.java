package com.demo.account.accountapp.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "client")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private  String lastName;

    @Column(name = "primary_address")
    private String primaryAddress;

    @Column(name = "secondary_address")
    private String secondaryAddress;

    @Column(name = "created_on")
    private LocalDateTime createdTime;

    @Column(name = "last_updated_on")
    private LocalDateTime updateTime;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private Set<AccountEntity> accounts;



}
