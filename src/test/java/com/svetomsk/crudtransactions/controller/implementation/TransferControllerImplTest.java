package com.svetomsk.crudtransactions.controller.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svetomsk.crudtransactions.dto.TransferCodeDto;
import com.svetomsk.crudtransactions.dto.TransferDto;
import com.svetomsk.crudtransactions.enums.TransferCurrency;
import com.svetomsk.crudtransactions.model.CreateTransferRequest;
import com.svetomsk.crudtransactions.model.IssueTransferRequest;
import com.svetomsk.crudtransactions.model.ListTransfersRequest;
import com.svetomsk.crudtransactions.model.TransfersListResponse;
import com.svetomsk.crudtransactions.service.interfaces.TransferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.svetomsk.crudtransactions.Utils.performRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferControllerImpl.class)
public class TransferControllerImplTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransferService transferService;

    @Test
    public void createTransfer_noAuth_unauthorized() throws Exception {
        var request = new CreateTransferRequest(null, null, 200.0, TransferCurrency.KGS, "", 1L);
        performRequest(mvc, post("/transfer/create")
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void createTransfer_withAuth_createTransferCalled() throws Exception {
        var request = new CreateTransferRequest(null, null, 200.0, TransferCurrency.KGS, "", 1L);
        when(transferService.createTransfer(any())).thenReturn(new TransferCodeDto());
        performRequest(mvc, post("/transfer/create")
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        verify(transferService, times(1)).createTransfer(request);
    }

    @Test
    public void issueTransfer_noAuth_unauthorized() throws Exception {
        performRequest(mvc, post("/transfer/issue")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new IssueTransferRequest())))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser
    public void issueTransfer_withAuth_issueTransferCalled() throws Exception {
        var request = new IssueTransferRequest();
        when(transferService.issueTransfer(any())).thenReturn(new TransferDto());
        performRequest(mvc, post("/transfer/issue")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new IssueTransferRequest())))
                .andExpect(status().isOk());
        verify(transferService, times(1)).issueTransfer(request);
    }

    @Test
    public void listTransfers_noAuth_unauthorized() throws Exception {
        performRequest(mvc, post("/transfer/list")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void listTransfers_withAuth_listTransfersCalled() throws Exception {
        when(transferService.listTransfers(any())).thenReturn(new TransfersListResponse());
        var request = new ListTransfersRequest();
        performRequest(mvc, post("/transfer/list")
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        verify(transferService, times(1)).listTransfers(request);
    }
}