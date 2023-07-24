package com.svetomsk.crudtransactions.service.interfaces;

import com.svetomsk.crudtransactions.dto.CashDeskDto;
import com.svetomsk.crudtransactions.model.CreateCashDeskRequest;
import org.springframework.stereotype.Service;

@Service
public interface CashDeskService {
    CashDeskDto createCashDesk(CashDeskDto request);

    CashDeskDto getCashDesks();

    CashDeskDto getCashDeskById();
}
