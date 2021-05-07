package com.nubiform.payment.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubiform.payment.repository.SentRepository;
import com.nubiform.payment.vo.CancelRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerCancelTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SentRepository sentRepository;

    @Autowired
    ObjectMapper objectMapper;

    CancelRequest cancelRequest;
    
    @BeforeEach
    void setUp() {
        cancelRequest = new CancelRequest();
        cancelRequest.setId(1L);
        cancelRequest.setAmount(1000L);
    }

    @Test
    public void delPayment() throws Exception {
        mockMvc.perform(delete("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cancelRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}