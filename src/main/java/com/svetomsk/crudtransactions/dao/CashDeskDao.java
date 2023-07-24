package com.svetomsk.crudtransactions.dao;

import com.svetomsk.crudtransactions.dto.CashDeskDto;
import com.svetomsk.crudtransactions.entity.CashDeskEntity;
import com.svetomsk.crudtransactions.repository.CashDeskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CashDeskDao {
    private final CashDeskRepository repository;

    public CashDeskDto save(CashDeskDto request) {
        var entity = CashDeskEntity.builder()
                .balance(request.getBalance())
                .build();
        return CashDeskDto.entityToDto(repository.save(entity));
    }

    public List<CashDeskDto> findAllCashDesks() {
        return repository.findAll()
                .stream()
                .map(CashDeskDto::entityToDto)
                .collect(Collectors.toList());
    }

    public CashDeskDto findById(Long id) {
        var entity = repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Desk with id " + id + " is not found"));
        return CashDeskDto.entityToDto(entity);
    }
}
