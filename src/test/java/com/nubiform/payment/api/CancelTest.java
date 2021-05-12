package com.nubiform.payment.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubiform.payment.controller.PaymentController;
import com.nubiform.payment.domain.History;
import com.nubiform.payment.repository.BalanceRepository;
import com.nubiform.payment.repository.HistoryRepository;
import com.nubiform.payment.repository.SentRepository;
import com.nubiform.payment.service.PaymentService;
import com.nubiform.payment.vo.CancelRequest;
import com.nubiform.payment.vo.Id;
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
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CancelTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PaymentService paymentService;

    @Autowired
    BalanceRepository balanceRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    SentRepository sentRepository;

    @Autowired
    ObjectMapper objectMapper;

    SubmitRequest submitRequest;
    CancelRequest cancelRequest;

    @BeforeEach
    void setUp() throws Exception {
        submitRequest = new SubmitRequest();
        submitRequest.setCard("1234567890123456");
        submitRequest.setExpiration("1234");
        submitRequest.setCvc("123");
        submitRequest.setInstallment(0);
        submitRequest.setAmount(10000L);

        Long id = paymentService.submit(submitRequest).getId();

        cancelRequest = new CancelRequest();
        cancelRequest.setId(Id.convert(id));
        cancelRequest.setAmount(10000L);
    }

    @Test
    @Transactional
    public void delPayment() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(PaymentController.API_V1_PAYMENT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cancelRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        TestResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TestResponse.class);
        History history = historyRepository.findById(response.getLongId()).orElse(null);

        assertEquals(submitRequest.getAmount() - cancelRequest.getAmount(), history.getBalance().getRemainAmount());
    }

    @Test
    public void delPaymentFailure() throws Exception {
        cancelRequest.setAmount(1000000L);

        mockMvc.perform(delete(PaymentController.API_V1_PAYMENT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cancelRequest)))
                .andDo(print())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists());
    }
}