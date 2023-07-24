package com.svetomsk.crudtransactions.controller;

import com.svetomsk.crudtransactions.dto.CashDeskDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cashDesk")
public interface CashDeskController {
    @PutMapping
    ResponseEntity<CashDeskDto> createCashDesk(@Valid CashDeskDto request);

    @GetMapping("/list")
    ResponseEntity<List<CashDeskDto>> getAllCashDesks();

    @GetMapping("/{deskId}")
    ResponseEntity<CashDeskDto> getCashDesk(@PathVariable("deskId") Long cashDeskId);
}
