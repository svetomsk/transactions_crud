package com.svetomsk.crudtransactions.dto;

import com.svetomsk.crudtransactions.enums.TransferCurrency;
import com.svetomsk.crudtransactions.enums.TransferStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferDto {
    private Long id;
    private UserDto sender;
    private Double amount;
    private TransferCurrency currency;
    private UserDto receiver;
    private String comment;
    private TransferStatus status;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;
    private Long cashDesk;
}
