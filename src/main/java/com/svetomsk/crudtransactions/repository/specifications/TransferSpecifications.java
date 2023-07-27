package com.svetomsk.crudtransactions.repository.specifications;

import com.svetomsk.crudtransactions.entity.TransferEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class TransferSpecifications {
    public static Specification<TransferEntity> createComplexPredicate(List<Specification<TransferEntity>> specifications) {
        return (root, criteriaQuery, builder) -> {
            var predicates = specifications.stream()
                    .map(value -> value.toPredicate(root, criteriaQuery, builder))
                    .toList();

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
