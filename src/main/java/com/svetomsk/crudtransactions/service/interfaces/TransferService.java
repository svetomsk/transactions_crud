package com.svetomsk.crudtransactions.service.interfaces;

import com.svetomsk.crudtransactions.dto.TransferDto;

public interface TransferService {
    TransferDto createTransfer();

    TransferDto issueTransfer();
}
