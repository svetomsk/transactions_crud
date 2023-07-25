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

    public CashDeskEntity findEntityById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Desk with id " + id + " is not found"));
    }

    public CashDeskDto findById(Long id) {
        return CashDeskDto.entityToDto(findEntityById(id));
    }

    public void withdraw(CashDeskEntity entity, double amount) {
        entity.setBalance(entity.getBalance() - amount);
        repository.save(entity);
    }

    public void deposit(CashDeskEntity entity, Double amount) {
        entity.setBalance(entity.getBalance() + amount);
        repository.save(entity);
    }
}
