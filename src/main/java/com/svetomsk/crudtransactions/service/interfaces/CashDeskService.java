package com.svetomsk.crudtransactions.service.interfaces;

import com.svetomsk.crudtransactions.dto.CashDeskDto;
import com.svetomsk.crudtransactions.model.CreateCashDeskRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CashDeskService {
    CashDeskDto createCashDesk(CashDeskDto request);

    List<CashDeskDto> getCashDesks();

    CashDeskDto getCashDeskById(Long id);
}
