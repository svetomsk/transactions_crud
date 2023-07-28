package com.svetomsk.crudtransactions.controller.implementation;

import com.svetomsk.crudtransactions.controller.interfaces.CashDeskController;
import com.svetomsk.crudtransactions.dto.CashDeskDto;
import com.svetomsk.crudtransactions.service.interfaces.CashDeskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cashDesk")
public class CashDeskControllerImpl implements CashDeskController {
    private final CashDeskService cashDeskService;
    @Override
    public ResponseEntity<CashDeskDto> createCashDesk() {
        return new ResponseEntity<>(cashDeskService.createCashDesk(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<CashDeskDto>> getAllCashDesks() {
        return new ResponseEntity<>(cashDeskService.getCashDesks(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CashDeskDto> getCashDesk(Long cashDeskId) {
        return new ResponseEntity<>(cashDeskService.getCashDeskById(cashDeskId), HttpStatus.OK);
    }
}
