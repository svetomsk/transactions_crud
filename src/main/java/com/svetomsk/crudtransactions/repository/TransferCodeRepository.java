package com.svetomsk.crudtransactions.repository;

import com.svetomsk.crudtransactions.entity.TransferCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransferCodeRepository extends JpaRepository<TransferCodeEntity, Long> {
    Optional<TransferCodeEntity> findByCode(String code);
}
