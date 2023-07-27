package com.svetomsk.crudtransactions.dto;

import jakarta.validation.constraints.Min;
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

    @Min(value = 0, message = "Cash desk balance should be non-negative")
    private Double balance;
}
