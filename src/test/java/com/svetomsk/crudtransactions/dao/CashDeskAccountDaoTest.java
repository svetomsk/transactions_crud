package com.svetomsk.crudtransactions.dao;

import com.svetomsk.crudtransactions.entity.CashDeskAccountEntity;
import com.svetomsk.crudtransactions.entity.CashDeskEntity;
import com.svetomsk.crudtransactions.enums.TransferCurrency;
import com.svetomsk.crudtransactions.repository.CashDeskAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CashDeskAccountDaoTest {
    @Mock
    private CashDeskAccountRepository repository;

    @InjectMocks
    private CashDeskAccountDao dao;

    @Test
    public void saveAccount_validAccountEntity_entitySaved() {
        var accountEntityCaptor = ArgumentCaptor.forClass(CashDeskAccountEntity.class);
        when(repository.save(accountEntityCaptor.capture())).thenAnswer(value -> value.getArguments()[0]);
        double balance = 1000.0;
        TransferCurrency currency = TransferCurrency.USD;
        var cashDeskEntity = CashDeskEntity.builder()
                .id(123L)
                .build();
        var entityToSave = CashDeskAccountEntity.builder()
                .balance(balance)
                .currency(currency)
                .cashDesk(cashDeskEntity)
                .build();

        var actual = dao.save(entityToSave);
        assertEquals(entityToSave, actual);

        var value = accountEntityCaptor.getValue();
        assertEquals(balance, value.getBalance());
        assertEquals(currency, value.getCurrency());
        assertEquals(cashDeskEntity, value.getCashDesk());
        assertTrue(value.isEnabled());

        verify(repository, times(1)).save(entityToSave);
    }

    @Test
    public void findAndWithdraw_accountNotFound_exceptionThrown() {
        when(repository.findByCashDeskAndCurrency(any(), any())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            dao.findAndWithdraw(new CashDeskEntity(), TransferCurrency.KGS, 1);
        });
    }

    @Test
    public void findAndWithDraw_accountFoundButBalanceIsLow_exceptionThrown() {
        double amount = 300.0;
        var account = CashDeskAccountEntity.builder().balance(amount / 2).build();
        when(repository.findByCashDeskAndCurrency(any(), any())).thenReturn(Optional.of(account));
        assertThrows(IllegalArgumentException.class, () -> {
            dao.findAndWithdraw(new CashDeskEntity(), TransferCurrency.KGS, amount);
        });
    }

    @Test
    public void findAndWithdraw_accountFoundAndBalanceOk_accountWithdrew() {
        double balance = 300.0;
        double amount = 100.0;
        var account = CashDeskAccountEntity.builder().balance(balance).build();
        when(repository.findByCashDeskAndCurrency(any(), any())).thenReturn(Optional.ofNullable(account));
        var accountCaptor = ArgumentCaptor.forClass(CashDeskAccountEntity.class);
        when(repository.save(accountCaptor.capture())).thenReturn(new CashDeskAccountEntity());
        dao.findAndWithdraw(new CashDeskEntity(), TransferCurrency.KGS, amount);
        var value = accountCaptor.getValue();
        assertEquals(balance - amount, value.getBalance());
        verify(repository, times(1)).findByCashDeskAndCurrency(any(), any());
        verify(repository, times(1)).save(any());
    }

    @Test
    public void findAndDeposit_accountNotFound_exceptionThrown() {
        when(repository.findByCashDeskAndCurrency(any(), any())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            dao.findAndDeposit(new CashDeskEntity(), TransferCurrency.KGS, 1);
        });
    }

    @Test
    public void findAndDeposit_accountFound_depositSaved() {
        double amount = 1000.0;
        double balance = 500.0;
        CashDeskAccountEntity account = CashDeskAccountEntity.builder().balance(balance).build();
        when(repository.findByCashDeskAndCurrency(any(), any())).thenReturn(Optional.of(account));
        var accountCaptor = ArgumentCaptor.forClass(CashDeskAccountEntity.class);
        when(repository.save(accountCaptor.capture())).thenAnswer(value -> value.getArguments()[0]);
        dao.findAndDeposit(new CashDeskEntity(), TransferCurrency.KGS, amount);
        var value = accountCaptor.getValue();
        assertEquals(balance + amount, value.getBalance());
        verify(repository, times(1)).findByCashDeskAndCurrency(any(), any());
        verify(repository, times(1)).save(any());
    }

    @Test
    public void findAllByCashDesk_cashDeskProvided_allAccountReturned() {
        var accounts = new ArrayList<CashDeskAccountEntity>();
        accounts.add(CashDeskAccountEntity.builder().id(1L).build());
        accounts.add(CashDeskAccountEntity.builder().id(2L).build());
        accounts.add(CashDeskAccountEntity.builder().id(3L).build());

        when(repository.findAllByCashDesk(any())).thenReturn(accounts);

        var actual = dao.findAllByCashDesk(new CashDeskEntity());
        assertEquals(accounts, actual);
        verify(repository, times(1)).findAllByCashDesk(any());
    }

}