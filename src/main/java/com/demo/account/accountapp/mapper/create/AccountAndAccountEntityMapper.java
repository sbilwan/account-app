package com.demo.account.accountapp.mapper.create;

import com.demo.account.accountapp.dao.entity.AccountEntity;
import com.demo.account.accountapp.dao.entity.ClientEntity;
import com.demo.account.accountapp.model.Account;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class AccountAndAccountEntityMapper {

    public abstract Account accountEntityToAccount(AccountEntity accountEntity);

    @Mapping(source = "clientEntity", target = "client")
    @Mapping(source = "account.id", target = "id")
    public abstract AccountEntity accountToAccountEntity(Account account, ClientEntity clientEntity);

    @AfterMapping
    void setDates(@MappingTarget AccountEntity accountEntity) {
        accountEntity.setCreatedTime(LocalDateTime.now());
        accountEntity.setUpdateTime(LocalDateTime.now());

    }

}
