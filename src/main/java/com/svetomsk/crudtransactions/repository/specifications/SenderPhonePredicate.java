package com.svetomsk.crudtransactions.repository.specifications;

import com.svetomsk.crudtransactions.entity.TransferEntity;
import com.svetomsk.crudtransactions.entity.UserEntity;
import com.svetomsk.crudtransactions.model.ListTransfersRequest;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class SenderPhonePredicate implements PredicateWithCondition<ListTransfersRequest, TransferEntity> {
    @Override
    public Specification<TransferEntity> makeSpecification(ListTransfersRequest value) {
        if (value.getSender() == null || value.getSender().getPhoneNumber() == null) return null;
        String phone = value.getSender().getPhoneNumber();
        return (root, criteriaQuery, builder) -> {
            Join<TransferEntity, UserEntity> joinUser = root.join("sender");
            return builder.equal(joinUser.get("phone"), phone);
        };
    }
}
