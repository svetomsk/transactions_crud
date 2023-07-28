package com.svetomsk.crudtransactions.repository;

import com.svetomsk.crudtransactions.entity.CashDeskAccountEntity;
import com.svetomsk.crudtransactions.entity.CashDeskEntity;
import com.svetomsk.crudtransactions.enums.TransferCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CashDeskAccountRepository extends JpaRepository<CashDeskAccountEntity, Long> {
    Optional<CashDeskAccountEntity> findByCashDeskAndCurrency(CashDeskEntity entity, TransferCurrency currency);

    List<CashDeskAccountEntity> findAllByCashDesk(CashDeskEntity cashDesk);
}
