package com.svetomsk.crudtransactions.repository;

import com.svetomsk.crudtransactions.entity.CashDesk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CashDeskRepository extends JpaRepository<CashDesk, Long> {

}
