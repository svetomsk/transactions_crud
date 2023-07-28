package com.svetomsk.crudtransactions.controller.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svetomsk.crudtransactions.Utils;
import com.svetomsk.crudtransactions.dto.CashDeskDto;
import com.svetomsk.crudtransactions.service.interfaces.CashDeskService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static com.svetomsk.crudtransactions.Utils.performRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(CashDeskControllerImpl.class)
public class CashDeskControllerImplTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CashDeskService cashDeskService;

    @Test
    @WithMockUser
    public void createCashDesk_correctRequest_serviceCalled() throws Exception {
        var request = new CashDeskDto();
        when(cashDeskService.createCashDesk()).thenReturn(request);
        performRequest(mvc, post("/cashDesk/create")
                .with(csrf())
                .content(Utils.stringify(mapper, request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
        verify(cashDeskService, times(1)).createCashDesk();
    }

    @Test
    public void createCashDesk_noAuth_unauthorized() throws Exception {
        performRequest(mvc, post("/cashDesk/create")
                .with(csrf())
                .content(Utils.stringify(mapper, new CashDeskDto()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void getCashDesks_correctRequest_serviceCalled() throws Exception {
        var serviceResult = new ArrayList<CashDeskDto>();
        serviceResult.add(new CashDeskDto());
        when(cashDeskService.getCashDesks()).thenReturn(serviceResult);
        var mvcResult = performRequest(mvc, get("/cashDesk/list"))
                .andExpect(status().isOk())
                .andReturn();
        verify(cashDeskService, times(1)).getCashDesks();
    }

    @Test
    public void getCashDesk_noAuth_unauthorized() throws Exception {
        performRequest(mvc, get("/cashDesk/list"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void getCashDeskById_correctRequest_entityReturned() throws Exception {
        var serviceResult = new CashDeskDto();
        when(cashDeskService.getCashDeskById(any())).thenReturn(serviceResult);
        var id = 1L;
        performRequest(mvc, get("/cashDesk/" + id))
                .andExpect(status().isOk())
                .andReturn();
        verify(cashDeskService, times(1)).getCashDeskById(id);
    }

    @Test
    public void getCashDeskById_noAuth_unauthorized() throws Exception {
        performRequest(mvc, get("/cashDesk/1"))
                .andExpect(status().isUnauthorized());
    }


}