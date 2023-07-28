package com.svetomsk.crudtransactions.components;


import com.svetomsk.crudtransactions.dao.CashDeskAccountDao;
import com.svetomsk.crudtransactions.dao.CashDeskDao;
import com.svetomsk.crudtransactions.dto.CashDeskDto;
import com.svetomsk.crudtransactions.entity.CashDeskAccountEntity;
import com.svetomsk.crudtransactions.entity.CashDeskEntity;
import com.svetomsk.crudtransactions.enums.TransferCurrency;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseInit {
    private final CashDeskDao cashDeskDao;
    private final CashDeskAccountDao accountDao;
    private final int minimumCashDesksCount = 2;
    private final double initialBalance = 10000.0;

    @PostConstruct
    public void createInitialDesks() {
        long cashDeskCount = cashDeskDao.count();
        for (long i = cashDeskCount; i < minimumCashDesksCount; i++) {
            log.info("Default cash desk added");
            CashDeskEntity cashDesk = cashDeskDao.saveEntity(CashDeskDto.builder().build());
            CashDeskAccountEntity kgsAccount = CashDeskAccountEntity.builder()
                    .currency(TransferCurrency.KGS)
                    .balance(initialBalance)
                    .cashDesk(cashDesk)
                    .build();
            accountDao.save(kgsAccount);
            CashDeskAccountEntity usdAccount = CashDeskAccountEntity.builder()
                    .currency(TransferCurrency.USD)
                    .balance(initialBalance)
                    .cashDesk(cashDesk)
                    .build();
            accountDao.save(usdAccount);
        }
    }
}
