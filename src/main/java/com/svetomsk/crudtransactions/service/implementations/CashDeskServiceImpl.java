package com.svetomsk.crudtransactions.service.implementations;

import com.svetomsk.crudtransactions.dao.CashDeskAccountDao;
import com.svetomsk.crudtransactions.dao.CashDeskDao;
import com.svetomsk.crudtransactions.dto.CashDeskAccountDto;
import com.svetomsk.crudtransactions.dto.CashDeskDto;
import com.svetomsk.crudtransactions.entity.CashDeskEntity;
import com.svetomsk.crudtransactions.service.interfaces.CashDeskService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CashDeskServiceImpl implements CashDeskService {
    private final CashDeskDao cashDeskDao;
    private final CashDeskAccountDao accountDao;
    private final ModelMapper modelMapper;
    @Override
    public CashDeskDto createCashDesk() {
        return cashDeskDao.createCashDesk();
    }

    @Override
    public List<CashDeskDto> getCashDesks() {
        return cashDeskDao.findAllCashDesks();
    }

    @Override
    public CashDeskDto getCashDeskById(Long id) {
        CashDeskEntity cashDesk = cashDeskDao.findEntityById(id);
        List<CashDeskAccountDto> accounts = accountDao.findAllByCashDesk(cashDesk)
                .stream()
                .map(value -> modelMapper.map(value, CashDeskAccountDto.class))
                .toList();
        CashDeskDto cashDeskDto = modelMapper.map(cashDesk, CashDeskDto.class);
        cashDeskDto.setAccounts(accounts);
        return cashDeskDto;
    }
}
