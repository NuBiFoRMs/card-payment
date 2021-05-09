package com.nubiform.payment.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubiform.payment.repository.SentRepository;
import com.nubiform.payment.service.PaymentService;
import com.nubiform.payment.vo.PaymentRequest;
import com.nubiform.payment.vo.SubmitRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PaymentService paymentService;

    @Autowired
    SentRepository sentRepository;

    @Autowired
    ObjectMapper objectMapper;

    SubmitRequest submitRequest;
    PaymentRequest paymentRequest;

    @BeforeEach
    void setUp() throws Exception {
        submitRequest = new SubmitRequest();
        submitRequest.setCard("1234567890123456");
        submitRequest.setExpiration("1234");
        submitRequest.setCvc("123");
        submitRequest.setInstallment(0);
        submitRequest.setAmount(1000L);

        Long id = paymentService.submit(submitRequest).getId();

        paymentRequest = new PaymentRequest();
        paymentRequest.setId(id);
    }

    @Test
    public void getPayment() throws Exception {
        mockMvc.perform(get("/api/v1/payment")
                .param("id", paymentRequest.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}