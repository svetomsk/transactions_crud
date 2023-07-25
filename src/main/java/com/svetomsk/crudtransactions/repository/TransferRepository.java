package com.svetomsk.crudtransactions.repository;

import com.svetomsk.crudtransactions.entity.TransferEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransferRepository extends JpaRepository<TransferEntity, Long>, JpaSpecificationExecutor<TransferEntity> {
    @Override
    Page<TransferEntity> findAll(Specification<TransferEntity> specification, Pageable pageable);
}
