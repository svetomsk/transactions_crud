package com.svetomsk.crudtransactions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svetomsk.crudtransactions.Utils;
import com.svetomsk.crudtransactions.controller.implementation.CashDeskControllerImpl;
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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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

    //    @Test
//    @WithMockUser
    public void createCashDesk_correctRequest_serviceCalled() throws Exception {
        var request = new CashDeskDto(1L, 20.0);
        when(cashDeskService.createCashDesk(any())).thenReturn(request);
        performRequest(post("/cashDesk/create")
                .content(Utils.stringify(mapper, request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
        verify(cashDeskService, times(1)).createCashDesk(any());
    }

    @Test
    @WithMockUser
    public void getCashDesks_correctRequest_serviceCalled() throws Exception {
        var serviceResult = new ArrayList<CashDeskDto>();
        serviceResult.add(new CashDeskDto(1L, 10.0));
        when(cashDeskService.getCashDesks()).thenReturn(serviceResult);
        var mvcResult = performRequest(get("/cashDesk/list"))
                .andExpect(status().isOk())
                .andReturn();
        verify(cashDeskService, times(1)).getCashDesks();
    }

    @Test
    @WithMockUser
    public void getCashDeskById_correctRequest_entityReturned() throws Exception {
        var serviceResult = new CashDeskDto(1L, 10.0);
        when(cashDeskService.getCashDeskById(any())).thenReturn(serviceResult);
        var id = 1L;
        performRequest(get("/cashDesk/" + id))
                .andExpect(status().isOk())
                .andReturn();
        verify(cashDeskService, times(1)).getCashDeskById(id);
    }

    private ResultActions performRequest(RequestBuilder builder) {
        try {
            return mvc.perform(builder);
        } catch (Exception exc) {
            log.info("Exception during mock request: " + exc.getMessage());
            throw new IllegalArgumentException("Request failed");
        }
    }

}