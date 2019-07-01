package com.demo.account.accountapp.model;


import com.demo.account.accountapp.dao.entity.AccountStatus;
import com.demo.account.accountapp.dao.entity.AccountType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account {

    @JsonProperty("account_id")
    private final Long id;

    private final BigDecimal balance;

    @JsonProperty("status")
    private final AccountStatus accountStatus;

    @JsonProperty("type")
    private final AccountType accountType;

    @JsonProperty("overdraw_limit")
    private final BigDecimal overDrawLimit;

}
