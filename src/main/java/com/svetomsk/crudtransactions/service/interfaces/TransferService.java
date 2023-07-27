package com.svetomsk.crudtransactions.service.interfaces;

import com.svetomsk.crudtransactions.dto.TransferCodeDto;
import com.svetomsk.crudtransactions.dto.TransferDto;
import com.svetomsk.crudtransactions.model.CreateTransferRequest;
import com.svetomsk.crudtransactions.model.IssueTransferRequest;
import com.svetomsk.crudtransactions.model.ListTransfersRequest;
import com.svetomsk.crudtransactions.model.TransfersListResponse;

public interface TransferService {
    TransferCodeDto createTransfer(CreateTransferRequest request);

    TransferDto issueTransfer(IssueTransferRequest request);

    TransfersListResponse listTransfers(ListTransfersRequest request);
}
