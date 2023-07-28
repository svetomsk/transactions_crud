package com.svetomsk.crudtransactions.service.implementations;

import com.svetomsk.crudtransactions.enums.TransferCurrency;
import com.svetomsk.crudtransactions.service.interfaces.CurrencyExchangeService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.EnumMap;

import static com.svetomsk.crudtransactions.enums.TransferCurrency.*;

@Slf4j
@Service
public class FixedCurrencyExchangeServiceImpl implements CurrencyExchangeService {
    private final TransferCurrency basicCurrency = USD;
    private EnumMap<TransferCurrency, Double> currencyRatios;

    @PostConstruct
    public void initRatios() {
        currencyRatios = new EnumMap<>(TransferCurrency.class);
        currencyRatios.put(KGS, 0.011);
        currencyRatios.put(USD, 1.0);
        currencyRatios.put(EUR, 1.11);
        currencyRatios.put(RUB, 0.011);
    }

    @Override
    public double convert(TransferCurrency currency, double amount, TransferCurrency targetCurrency) {
        if (currency == targetCurrency) return amount;
        double firstRatio = currencyRatios.get(currency);
        double targetRatio = currencyRatios.get(targetCurrency);
        return amount * firstRatio / targetRatio;
    }

    @Override
    public void updateCurrencies() {
        log.info("Updating currencies..");
        log.info("Currencies were updated");
    }
}
