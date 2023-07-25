package com.svetomsk.crudtransactions.controller.interfaces;

import com.svetomsk.crudtransactions.dto.TransferCodeDto;
import com.svetomsk.crudtransactions.dto.TransferDto;
import com.svetomsk.crudtransactions.model.CreateTransferRequest;
import com.svetomsk.crudtransactions.model.IssueTransferRequest;
import com.svetomsk.crudtransactions.model.ListTransfersRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transfer")
@PreAuthorize("isAuthenticated()")
public interface TransferController {
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    TransferCodeDto createTransfer(@RequestBody @Valid CreateTransferRequest request);

    @PostMapping(value = "/issue", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    TransferDto issueTransfer(@RequestBody @Valid IssueTransferRequest request);

    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    List<TransferDto> listTransfers(@RequestBody @Valid ListTransfersRequest request);
}
