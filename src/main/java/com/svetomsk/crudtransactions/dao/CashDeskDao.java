package com.svetomsk.crudtransactions.dao;

import com.svetomsk.crudtransactions.dto.CashDeskDto;
import com.svetomsk.crudtransactions.entity.CashDeskEntity;
import com.svetomsk.crudtransactions.repository.CashDeskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CashDeskDao {
    private final CashDeskRepository repository;
    private final ModelMapper modelMapper;

    public CashDeskDto createCashDesk() {
        return modelMapper.map(repository.save(new CashDeskEntity()), CashDeskDto.class);
    }

    public CashDeskEntity saveEntity(CashDeskDto cashDeskDto) {
        return repository.save(modelMapper.map(cashDeskDto, CashDeskEntity.class));
    }

    public List<CashDeskDto> findAllCashDesks() {
        return repository.findAll()
                .stream()
                .map(value -> modelMapper.map(value, CashDeskDto.class))
                .collect(Collectors.toList());
    }

    public CashDeskEntity findEntityById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Desk with id " + id + " is not found"));
    }

    public CashDeskDto findById(Long id) {
        return modelMapper.map(findEntityById(id), CashDeskDto.class);
    }


    @Transactional
    public CashDeskEntity findAndDeposit(Long entityId, Double amount) {
        CashDeskEntity entity = findEntityById(entityId);
//        entity.setBalance(entity.getBalance() + amount);
        return repository.save(entity);
    }

    public long count() {
        return repository.count();
    }
}
