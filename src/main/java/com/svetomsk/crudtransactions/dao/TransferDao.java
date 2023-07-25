package com.svetomsk.crudtransactions.dao;

import com.svetomsk.crudtransactions.entity.TransferEntity;
import com.svetomsk.crudtransactions.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransferDao {
    private final TransferRepository transferRepository;

    public TransferEntity saveTransfer(TransferEntity entity) {
        return transferRepository.save(entity);
    }
}
