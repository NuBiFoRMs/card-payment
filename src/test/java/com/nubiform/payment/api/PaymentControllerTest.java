package com.nubiform.payment.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void postPayment() throws Exception {
        mockMvc.perform(post("/api/v1/payment"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void delPayment() throws Exception {
        mockMvc.perform(delete("/api/v1/payment"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getPayment() throws Exception {
        mockMvc.perform(get("/api/v1/payment"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}