package com.svetomsk.crudtransactions.repository.specifications;

import org.springframework.data.jpa.domain.Specification;

public interface PredicateWithCondition<T, S> {
    Specification<S> makeSpecification(T value);
}
