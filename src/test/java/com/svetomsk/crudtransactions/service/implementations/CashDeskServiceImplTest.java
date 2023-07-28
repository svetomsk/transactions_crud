package com.svetomsk.crudtransactions.service.implementations;

import com.svetomsk.crudtransactions.dao.CashDeskAccountDao;
import com.svetomsk.crudtransactions.dao.CashDeskDao;
import com.svetomsk.crudtransactions.dto.CashDeskDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CashDeskServiceImplTest {
    @Mock
    private CashDeskDao cashDeskDao;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CashDeskAccountDao accountDao;

    @InjectMocks
    private CashDeskServiceImpl service;

    @Test
    public void createCashDesk_validRequest_daoObjectCalled() {
        var expected = new CashDeskDto();
        when(cashDeskDao.createCashDesk()).thenReturn(expected);
        var actual = service.createCashDesk();
        assertEquals(expected, actual);
        verify(cashDeskDao, times(1)).createCashDesk();
    }

    @Test
    public void getCashDesks_validRequest_daoObjectCalled() {
        var expected = new ArrayList<CashDeskDto>();
        expected.add(new CashDeskDto());
        expected.add(new CashDeskDto());
        expected.add(new CashDeskDto());
        when(cashDeskDao.findAllCashDesks()).thenReturn(expected);
        var actual = service.getCashDesks();
        assertEquals(expected, actual);
        verify(cashDeskDao, times(1)).findAllCashDesks();
    }

    //    @Test
    public void getCashDeskById_validRequest_daoObjectCalled() {
        var expected = new CashDeskDto();
        when(cashDeskDao.findById(any())).thenReturn(expected);
        when(accountDao.findAllByCashDesk(any())).thenReturn(new ArrayList<>());
        when(modelMapper.map(any(), any())).thenCallRealMethod();
        var actual = service.getCashDeskById(1L);
        assertEquals(expected, actual);
        verify(cashDeskDao, times(1)).findById(any());
    }

}