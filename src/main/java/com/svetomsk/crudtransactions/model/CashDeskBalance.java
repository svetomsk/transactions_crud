package com.svetomsk.crudtransactions.model;

import com.svetomsk.crudtransactions.enums.TransferCurrency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashDeskBalance {
    private TransferCurrency currency;
    private double amount;
}
