package com.svetomsk.crudtransactions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

@Slf4j
public class Utils {
    public static String stringify(ObjectMapper mapper, Object any) {
        try {
            return mapper.writeValueAsString(any);
        } catch (JsonProcessingException e) {
            log.error("Unable to convert object to string: " + e.getMessage());
        }
        // catch incorrect data and return exception
        return "";
    }

    public static ResultActions performRequest(MockMvc mvc, RequestBuilder builder) {
        try {
            return mvc.perform(builder);
        } catch (Exception exc) {
            log.info("Exception during mock request: " + exc.getMessage());
            throw new IllegalArgumentException("Request failed");
        }
    }
}
