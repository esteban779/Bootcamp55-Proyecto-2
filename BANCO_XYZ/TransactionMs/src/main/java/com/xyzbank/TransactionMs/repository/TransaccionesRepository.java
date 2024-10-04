package com.xyzbank.TransactionMs.repository;

import com.xyzbank.TransactionMs.entity.TransaccionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransaccionesRepository extends MongoRepository<TransaccionEntity, String> {
}
