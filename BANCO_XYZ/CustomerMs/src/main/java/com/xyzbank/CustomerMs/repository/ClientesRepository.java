package com.xyzbank.CustomerMs.repository;

import com.xyzbank.CustomerMs.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientesRepository extends JpaRepository<ClienteEntity, Long> {
}
