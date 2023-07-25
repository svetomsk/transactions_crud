package com.svetomsk.crudtransactions.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TransferOrderParam {
    CREATED_AT("createdAt"),
    ID("id"),
    SENDER("sender"),
    RECEIVER("receiver"),
    CASH_DESK("cashDesk");

    @Getter
    private final String column;
}
