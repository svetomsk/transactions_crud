package com.svetomsk.crudtransactions.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCashDeskRequest {
    @NotNull(message = "Cash desk balance should not be blank")
    @Min(value = 0, message = "Cash desk balance should be non-negative number")
    private Double initialBalance;
}
