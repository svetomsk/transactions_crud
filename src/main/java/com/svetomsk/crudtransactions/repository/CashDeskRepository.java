package com.svetomsk.crudtransactions.repository;

import com.svetomsk.crudtransactions.entity.CashDeskEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CashDeskRepository extends JpaRepository<CashDeskEntity, Long> {
}
