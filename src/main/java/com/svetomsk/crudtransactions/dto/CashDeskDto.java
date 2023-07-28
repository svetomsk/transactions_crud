package com.svetomsk.crudtransactions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashDeskDto {
    private Long id;

    private List<CashDeskAccountDto> accounts;
}
