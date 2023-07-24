package com.svetomsk.crudtransactions.dto;

import com.svetomsk.crudtransactions.entity.CashDeskEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashDeskDto {
    private Long id;
    private Double balance;
    public static CashDeskEntity dtoToEntity(CashDeskDto dto) {
        return CashDeskEntity.builder()
                .balance(dto.balance)
                .build();
    }

    public static CashDeskDto entityToDto(CashDeskEntity entity) {
        return CashDeskDto.builder()
                .id(entity.getId())
                .balance(entity.getBalance()).build();
    }
}
