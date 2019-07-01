package com.demo.account.accountapp.mapper.create;

import com.demo.account.accountapp.dao.entity.ClientEntity;
import com.demo.account.accountapp.model.Client;
import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ClientAndClientEntityMapper {

    @Mapping(source = "name", target = "firstName")
    @Mapping(source = "surname", target = "lastName")
    public abstract ClientEntity clientToClientEntity(Client client);

    @Mapping(source = "firstName", target = "name")
    @Mapping(source = "lastName", target = "surname")
    public abstract Client clientEntityToClient(ClientEntity clientEntity);

    @AfterMapping
    void setDates(@MappingTarget ClientEntity clientEntity) {
        clientEntity.setCreatedTime(LocalDateTime.now());
        clientEntity.setUpdateTime(LocalDateTime.now());
    }

}
