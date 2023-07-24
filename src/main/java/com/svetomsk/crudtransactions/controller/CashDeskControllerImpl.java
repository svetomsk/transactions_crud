package com.svetomsk.crudtransactions.controller;

import com.svetomsk.crudtransactions.dto.CashDeskDto;
import com.svetomsk.crudtransactions.service.interfaces.CashDeskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cashDesk")
public class CashDeskControllerImpl implements CashDeskController {
    private final CashDeskService cashDeskService;
    @Override
    public ResponseEntity<CashDeskDto> createCashDesk(@RequestBody @Valid CashDeskDto request) {
        return new ResponseEntity<>(cashDeskService.createCashDesk(request), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<CashDeskDto>> getAllCashDesks() {
        return new ResponseEntity<>(cashDeskService.getCashDesks(), HttpStatus.OK);
    }
}
