package com.svetomsk.crudtransactions.model;

import com.svetomsk.crudtransactions.dto.TransferDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransfersListResponse {
    private List<TransferDto> transfers;
    private Integer pageNumber;
    private Integer size;
}
