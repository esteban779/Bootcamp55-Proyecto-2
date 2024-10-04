package com.xyzbank.AccountMs.repository;

import com.xyzbank.AccountMs.entity.CuentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentasRepository extends JpaRepository<CuentaEntity, Long> {
    long countByClienteId(Long clienteId);
    Optional<CuentaEntity> findByClienteId(Long clienteId);
}
