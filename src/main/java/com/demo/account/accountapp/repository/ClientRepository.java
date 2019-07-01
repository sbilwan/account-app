package com.demo.account.accountapp.repository;

import com.demo.account.accountapp.dao.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository  extends JpaRepository<ClientEntity, Long> {
}
