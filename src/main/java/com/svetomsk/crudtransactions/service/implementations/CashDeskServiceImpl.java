package com.svetomsk.crudtransactions.service.implementations;

import com.svetomsk.crudtransactions.dao.CashDeskDao;
import com.svetomsk.crudtransactions.dto.CashDeskDto;
import com.svetomsk.crudtransactions.model.CreateCashDeskRequest;
import com.svetomsk.crudtransactions.service.interfaces.CashDeskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CashDeskServiceImpl implements CashDeskService {
    private final CashDeskDao cashDeskDao;
    @Override
    public CashDeskDto createCashDesk(CashDeskDto request) {
        return cashDeskDao.save(request);
    }

    @Override
    public List<CashDeskDto> getCashDesks() {
        return cashDeskDao.findAllCashDesks();
    }

    @Override
    public CashDeskDto getCashDeskById(Long id) {
        return cashDeskDao.findById(id);
    }
}
