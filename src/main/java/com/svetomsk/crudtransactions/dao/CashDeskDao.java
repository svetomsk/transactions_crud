package com.svetomsk.crudtransactions.dao;

import com.svetomsk.crudtransactions.dto.CashDeskDto;
import com.svetomsk.crudtransactions.entity.CashDeskEntity;
import com.svetomsk.crudtransactions.repository.CashDeskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CashDeskDao {
    private final CashDeskRepository repository;

    public CashDeskDto save(CashDeskDto request) {
        CashDeskEntity entity = new CashDeskEntity();
        entity.setBalance(request.getBalance());
        return CashDeskDto.entityToDto(repository.save(entity));
    }
}
