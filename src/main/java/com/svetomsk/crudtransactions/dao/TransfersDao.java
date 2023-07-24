package com.svetomsk.crudtransactions.dao;

import com.svetomsk.crudtransactions.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransfersDao {
    private final TransferRepository transferRepository;

}
