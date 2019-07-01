package com.demo.account.accountapp.mapper.create;

import com.demo.account.accountapp.dao.entity.TransactionEntity;
import com.demo.account.accountapp.model.Transfer;
import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class TransferAndTransactionEntityMapper {

    @Mapping(source = "sourceAccountId", target = "debitedAccountId")
    @Mapping(source = "destinationAccountId", target = "creditedAccountId")
    @Mapping(source = "transferAmount", target = "amount")
    @Mapping(source = "transferMessage", target = "message")
    public abstract TransactionEntity transferToTransactionEntity(Transfer transfer);

    @Mapping(source = "debitedAccountId", target = "sourceAccountId")
    @Mapping(source = "creditedAccountId", target = "destinationAccountId")
    @Mapping(source = "amount", target = "transferAmount")
    @Mapping(source = "createdTime", target = "transactionDateTime")
    @Mapping(source = "message", target = "transferMessage")
    public abstract Transfer transactionEntityToTransfer(TransactionEntity transactionEntity);

    @AfterMapping
    void setTransactionDateTime(@MappingTarget TransactionEntity transactionEntity) {
        transactionEntity.setCreatedTime(LocalDateTime.now());
    }
}
