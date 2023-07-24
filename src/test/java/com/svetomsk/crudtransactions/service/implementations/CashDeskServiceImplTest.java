package com.svetomsk.crudtransactions.service.implementations;

import com.svetomsk.crudtransactions.dao.CashDeskDao;
import com.svetomsk.crudtransactions.dto.CashDeskDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CashDeskServiceImplTest {
    @Mock
    private CashDeskDao cashDeskDao;

    @InjectMocks
    private CashDeskServiceImpl service;

    @Test
    public void createCashDesk_validRequest_daoObjectCalled() {
        var dto = new CashDeskDto();
        var expected = new CashDeskDto(1L, 12.0);
        when(cashDeskDao.save(dto)).thenReturn(expected);
        var actual = service.createCashDesk(dto);
        assertEquals(expected, actual);
        verify(cashDeskDao, times(1)).save(dto);
    }

    @Test
    public void getCashDesks_validRequest_daoObjectCalled() {
        var expected = new ArrayList<CashDeskDto>();
        expected.add(new CashDeskDto(1L, 10.0));
        expected.add(new CashDeskDto(2L, 20.0));
        expected.add(new CashDeskDto(3L, 30.0));
        when(cashDeskDao.findAllCashDesks()).thenReturn(expected);
        var actual = service.getCashDesks();
        assertEquals(expected, actual);
        verify(cashDeskDao, times(1)).findAllCashDesks();
    }

    @Test
    public void getCashDeskById_validRequest_daoObjectCalled() {
        var expected = new CashDeskDto(1L, 10.0);
        when(cashDeskDao.findById(any())).thenReturn(expected);
        var actual = service.getCashDeskById(1L);
        assertEquals(expected, actual);
        verify(cashDeskDao, times(1)).findById(any());
    }

}