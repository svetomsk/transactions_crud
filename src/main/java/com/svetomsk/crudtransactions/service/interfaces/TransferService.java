package com.svetomsk.crudtransactions.service.interfaces;

import com.svetomsk.crudtransactions.dto.TransferCodeDto;
import com.svetomsk.crudtransactions.dto.TransferDto;
import com.svetomsk.crudtransactions.model.CreateTransferRequest;
import com.svetomsk.crudtransactions.model.IssueTransferRequest;

public interface TransferService {
    TransferCodeDto createTransfer(CreateTransferRequest request);

    TransferDto issueTransfer(IssueTransferRequest request);
}
