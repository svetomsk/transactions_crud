package com.svetomsk.crudtransactions.service.interfaces;

import com.svetomsk.crudtransactions.dto.CashDeskDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CashDeskService {
    CashDeskDto createCashDesk();

    List<CashDeskDto> getCashDesks();

    CashDeskDto getCashDeskById(Long id);
}
