package com.svetomsk.crudtransactions.dto;

import com.svetomsk.crudtransactions.enums.TransferCurrency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashDeskAccountDto {
    private Long id;
    private TransferCurrency currency;
    private Double balance;
    private Long cashDeskId;
}
