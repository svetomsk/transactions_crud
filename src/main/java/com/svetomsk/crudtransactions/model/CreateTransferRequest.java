package com.svetomsk.crudtransactions.model;

import com.svetomsk.crudtransactions.dto.UserDto;
import com.svetomsk.crudtransactions.enums.TransferCurrency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTransferRequest {
    private UserDto senderInfo;
    private UserDto receiverInfo;
    private Double amount;
    private TransferCurrency currency;
    private String comment;
    private Long cashDeskId;
}
