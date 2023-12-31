package com.svetomsk.crudtransactions.controller.interfaces;

import com.svetomsk.crudtransactions.dto.CashDeskDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cashDesk")
@PreAuthorize("isAuthenticated()")
public interface CashDeskController {
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CashDeskDto> createCashDesk();

    @GetMapping("/list")
    ResponseEntity<List<CashDeskDto>> getAllCashDesks();

    @GetMapping("/{deskId}")
    ResponseEntity<CashDeskDto> getCashDesk(@PathVariable("deskId") Long cashDeskId);
}
