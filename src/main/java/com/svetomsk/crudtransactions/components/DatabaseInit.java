package com.svetomsk.crudtransactions.components;


import com.svetomsk.crudtransactions.dao.CashDeskDao;
import com.svetomsk.crudtransactions.dto.CashDeskDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseInit {
    private final CashDeskDao cashDeskDao;
    private final int minimumCashDesksCount = 2;
    private final double initialBalance = 10000.0;

    @PostConstruct
    public void createInitialDesks() {
        long cashDeskCount = cashDeskDao.count();
        for (long i = cashDeskCount; i <= minimumCashDesksCount; i++) {
            log.info("Default cash desk added");
            cashDeskDao.save(CashDeskDto.builder().balance(initialBalance).build());
        }
    }
}
