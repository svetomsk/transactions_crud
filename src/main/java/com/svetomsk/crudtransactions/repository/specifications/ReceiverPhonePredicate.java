package com.svetomsk.crudtransactions.repository.specifications;

import com.svetomsk.crudtransactions.dto.UserDto;
import com.svetomsk.crudtransactions.entity.TransferEntity;
import com.svetomsk.crudtransactions.entity.UserEntity;
import com.svetomsk.crudtransactions.model.ListTransfersRequest;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ReceiverPhonePredicate implements PredicateWithCondition<ListTransfersRequest, TransferEntity> {
    @Override
    public Specification<TransferEntity> makeSpecification(ListTransfersRequest value) {
        UserDto receiver = value.getReceiver();
        if (receiver == null || receiver.getPhone() == null) return null;
        return (root, criteriaQuery, builder) -> {
            Join<TransferEntity, UserEntity> joinUser = root.join("receiver");
            return builder.equal(joinUser.get("phone"), receiver.getPhone());
        };
    }
}
