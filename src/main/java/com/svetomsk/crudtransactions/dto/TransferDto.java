package com.svetomsk.crudtransactions.dto;

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
    private String sender;
    private String receiver;
    private Double amount;
    private String currency;
    private String senderPhone;
    private String receiverPhone;
    private String comment;
    private String status;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;
    private Long cashDesk;
}
