package com.svetomsk.crudtransactions.dao;

import com.svetomsk.crudtransactions.entity.CashDeskAccountEntity;
import com.svetomsk.crudtransactions.entity.CashDeskEntity;
import com.svetomsk.crudtransactions.enums.TransferCurrency;
import com.svetomsk.crudtransactions.repository.CashDeskAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class CashDeskAccountDao {
    private final CashDeskAccountRepository repository;

    public CashDeskAccountEntity save(CashDeskAccountEntity entity) {
        return repository.save(
                CashDeskAccountEntity
                        .builder()
                        .cashDesk(entity.getCashDesk())
                        .balance(entity.getBalance())
                        .currency(entity.getCurrency())
                        .build());
    }

    @Transactional
    public void findAndWithdraw(CashDeskEntity cashDesk, TransferCurrency currency, double amount) {
        CashDeskAccountEntity entity = repository.findByCashDeskAndCurrency(cashDesk, currency).orElseThrow(() ->
                new NoSuchElementException("Account is not found for cash desk " + cashDesk.getId() + " " +
                        "with currency " + currency));
        if (entity.getBalance() < amount) {
            throw new IllegalArgumentException("Not enough balance to withdraw on account " + entity.getId());
        }
        entity.setBalance(entity.getBalance() - amount);
        repository.save(entity);
    }

    @Transactional
    public CashDeskAccountEntity findAndDeposit(CashDeskEntity cashDesk, TransferCurrency currency, double amount) {
        CashDeskAccountEntity entity = repository.findByCashDeskAndCurrency(cashDesk, currency).orElseThrow(() ->
                new NoSuchElementException("Account is not found for cash desk " + cashDesk.getId() + " with currency " +
                        currency));
        entity.setBalance(entity.getBalance() + amount);
        return repository.save(entity);
    }

    public List<CashDeskAccountEntity> findAllByCashDesk(CashDeskEntity cashDesk) {
        return repository.findAllByCashDesk(cashDesk);
    }
}
