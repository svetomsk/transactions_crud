package com.svetomsk.crudtransactions.controller.implementation;

import com.svetomsk.crudtransactions.controller.interfaces.TransferController;
import com.svetomsk.crudtransactions.dto.TransferCodeDto;
import com.svetomsk.crudtransactions.dto.TransferDto;
import com.svetomsk.crudtransactions.model.CreateTransferRequest;
import com.svetomsk.crudtransactions.model.IssueTransferRequest;
import com.svetomsk.crudtransactions.model.ListTransfersRequest;
import com.svetomsk.crudtransactions.service.interfaces.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class TransferControllerImpl implements TransferController {
    private final TransferService transferService;

    @Override
    public TransferCodeDto createTransfer(CreateTransferRequest request) {
        return transferService.createTransfer(request);
    }

    @Override
    public TransferDto issueTransfer(IssueTransferRequest request) {
        return transferService.issueTransfer(request);
    }

    @Override
    public List<TransferDto> listTransfers(ListTransfersRequest request) {
        return transferService.listTransfers(request);
    }
}
