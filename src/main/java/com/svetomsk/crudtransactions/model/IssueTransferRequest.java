package com.svetomsk.crudtransactions.model;

import com.svetomsk.crudtransactions.dto.UserDto;
import com.svetomsk.crudtransactions.enums.TransferCurrency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueTransferRequest {
    private UserDto issuer;
    private String secretCode;
    private Long cashDeskId;
    private TransferCurrency currency;
}
