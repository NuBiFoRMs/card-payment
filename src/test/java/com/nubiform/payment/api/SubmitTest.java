package com.nubiform.payment.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubiform.payment.controller.PaymentController;
import com.nubiform.payment.domain.Balance;
import com.nubiform.payment.domain.History;
import com.nubiform.payment.domain.Sent;
import com.nubiform.payment.repository.BalanceRepository;
import com.nubiform.payment.repository.HistoryRepository;
import com.nubiform.payment.repository.SentRepository;
import com.nubiform.payment.vo.SubmitRequest;
import com.nubiform.payment.vo.TestResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SubmitTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BalanceRepository balanceRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    SentRepository sentRepository;

    @Autowired
    ObjectMapper objectMapper;

    SubmitRequest submitRequest;

    @BeforeEach
    void setUp() {
        submitRequest = new SubmitRequest();
        submitRequest.setCard("1234567890123456");
        submitRequest.setExpiration("1234");
        submitRequest.setCvc("123");
        submitRequest.setInstallment(0);
        submitRequest.setAmount(1000L);
    }

    @Test
    public void postPayment() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(PaymentController.API_V1_PAYMENT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.data").exists())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        TestResponse response = objectMapper.readValue(responseBody, TestResponse.class);
        Balance balance = balanceRepository.findById(response.getLongId()).orElse(null);
        History history = historyRepository.findById(response.getLongId()).orElse(null);
        Sent sent = sentRepository.findById(response.getLongId()).orElse(null);

        assertNotNull(balance);
        assertEquals(submitRequest.getAmount(), balance.getRemainAmount());

        assertNotNull(history);
        assertEquals(submitRequest.getAmount(), history.getAmount());

        assertNotNull(sent);
    }
}