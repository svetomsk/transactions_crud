package com.svetomsk.crudtransactions.repository;

import com.svetomsk.crudtransactions.entity.CashDeskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashDeskRepository extends JpaRepository<CashDeskEntity, Long> {
    @Override
    <S extends CashDeskEntity> S save(S entity);
}
