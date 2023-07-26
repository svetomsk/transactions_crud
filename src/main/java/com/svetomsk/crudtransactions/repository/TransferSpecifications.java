package com.svetomsk.crudtransactions.repository;

import com.svetomsk.crudtransactions.entity.TransferEntity;
import com.svetomsk.crudtransactions.entity.UserEntity;
import com.svetomsk.crudtransactions.enums.TransferStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class TransferSpecifications {

    public static Specification<TransferEntity> senderPredicate(String phone) {
        return (root, criteriaQuery, builder) -> {
            Join<TransferEntity, UserEntity> joinUser = root.join("sender");
            return builder.equal(joinUser.get("phone"), phone);
        };
    }

    public static Specification<TransferEntity> receiverPredicate(String phone) {
        return (root, criteriaQuery, builder) -> {
            Join<TransferEntity, UserEntity> joinUser = root.join("receiver");
            return builder.equal(joinUser.get("phone"), phone);
        };
    }

    public static Specification<TransferEntity> statusPredicate(TransferStatus status) {
        return (root, criteriaQuery, builder) -> builder.equal(root.get("status"), status);
    }

    public static Specification<TransferEntity> createComplexPredicate(List<Specification<TransferEntity>> specifications) {
        return (root, criteriaQuery, builder) -> {
            var predicates = specifications.stream()
                    .map(value -> value.toPredicate(root, criteriaQuery, builder))
                    .toList();

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
