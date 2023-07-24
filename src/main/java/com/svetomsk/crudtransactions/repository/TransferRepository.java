package com.svetomsk.crudtransactions.repository;

import com.svetomsk.crudtransactions.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<TransferEntity, Long> {
}
