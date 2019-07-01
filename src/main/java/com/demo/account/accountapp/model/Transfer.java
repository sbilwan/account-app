package com.demo.account.accountapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class Transfer {

    @JsonProperty("debit_account_id")
    private final Long sourceAccountId;

    @JsonProperty("credit_account_id")
    private final Long destinationAccountId;

    @JsonProperty("amount")
    private final BigDecimal transferAmount;

    @JsonProperty("message")
    private final String transferMessage;

    @JsonSerialize
    @JsonProperty("transaction_date")
    private final LocalDateTime transactionDateTime;
}
