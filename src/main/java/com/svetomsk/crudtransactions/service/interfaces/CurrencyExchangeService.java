package com.svetomsk.crudtransactions.service.interfaces;

import com.svetomsk.crudtransactions.enums.TransferCurrency;

public interface CurrencyExchangeService {
    double convert(TransferCurrency currency, double amount, TransferCurrency targetCurrency);

    void updateCurrencies();
}
