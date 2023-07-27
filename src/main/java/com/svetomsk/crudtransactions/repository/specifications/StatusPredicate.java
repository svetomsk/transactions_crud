package com.svetomsk.crudtransactions.repository.specifications;

import com.svetomsk.crudtransactions.entity.TransferEntity;
import com.svetomsk.crudtransactions.model.ListTransfersRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class StatusPredicate implements PredicateWithCondition<ListTransfersRequest, TransferEntity> {
    @Override
    public Specification<TransferEntity> makeSpecification(ListTransfersRequest value) {
        if (value.getStatus() == null) return null;
        return (root, criteriaQuery, builder) -> builder.equal(root.get("status"), value.getStatus());
    }
}
