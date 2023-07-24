package com.svetomsk.crudtransactions.controller;

import com.svetomsk.crudtransactions.dto.CashDeskDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cashDesk")
public interface CashDeskController {
    @PutMapping
    ResponseEntity<CashDeskDto> createCashDesk(@Valid CashDeskDto request);

    ResponseEntity<List<CashDeskDto>> getAllCashDesks();
}
