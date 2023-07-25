package com.svetomsk.crudtransactions.model;

import com.svetomsk.crudtransactions.dto.UserDto;
import com.svetomsk.crudtransactions.enums.TransferOrderParam;
import com.svetomsk.crudtransactions.enums.TransferStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListTransfersRequest {
    private int pageNumber = 0;
    private int pageSize = 10;
    private TransferOrderParam sortBy = TransferOrderParam.CREATED_AT;
    private Sort.Direction order = Sort.Direction.DESC;
    private UserDto sender;
    private UserDto receiver;
    private TransferStatus status;
}
