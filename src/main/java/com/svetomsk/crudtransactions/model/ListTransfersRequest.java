package com.svetomsk.crudtransactions.model;

import com.svetomsk.crudtransactions.dto.UserDto;
import com.svetomsk.crudtransactions.enums.TransferOrderParam;
import com.svetomsk.crudtransactions.enums.TransferStatus;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListTransfersRequest {
    @Min(value = 1, message = "Page should be at least 1")
    private int pageNumber = 1;
    private int pageSize = 10;
    private TransferOrderParam sortBy = TransferOrderParam.CREATED_AT;
    private Sort.Direction order = Sort.Direction.DESC;
    private UserDto sender;
    private UserDto receiver;
    private TransferStatus status;
}
