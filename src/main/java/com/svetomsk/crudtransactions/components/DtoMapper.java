package com.svetomsk.crudtransactions.components;

import com.svetomsk.crudtransactions.dto.CashDeskDto;
import com.svetomsk.crudtransactions.dto.TransferDto;
import com.svetomsk.crudtransactions.dto.UserDto;
import com.svetomsk.crudtransactions.entity.CashDeskEntity;
import com.svetomsk.crudtransactions.entity.TransferEntity;
import com.svetomsk.crudtransactions.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {
    public UserDto toDto(UserEntity entity) {
        return UserDto.builder()
                .phoneNumber(entity.getPhone())
                .name(entity.getName())
                .build();
    }

    public TransferDto toDto(TransferEntity entity) {
        return TransferDto.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .cashDesk(entity.getCashDesk().getId())
                .sender(toDto(entity.getSender()))
                .receiver(toDto(entity.getReceiver()))
                .comment(entity.getComment())
                .status(entity.getStatus())
                .createAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public CashDeskDto toDto(CashDeskEntity entity) {
        return CashDeskDto.builder()
                .id(entity.getId())
                .balance(entity.getBalance()).build();
    }
}
