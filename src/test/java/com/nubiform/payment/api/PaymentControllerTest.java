package com.nubiform.payment.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubiform.payment.domain.Sent;
import com.nubiform.payment.repository.SentRepository;
import com.nubiform.payment.vo.CancelRequest;
import com.nubiform.payment.vo.PaymentRequest;
import com.nubiform.payment.vo.Response;
import com.nubiform.payment.vo.SubmitRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SentRepository sentRepository;

    @Autowired
    ObjectMapper objectMapper;

    SubmitRequest submitRequest;
    CancelRequest cancelRequest;
    PaymentRequest paymentRequest;

    @BeforeEach
    void setUp() {
        submitRequest = new SubmitRequest();
        submitRequest.setCard("1234567890123456");
        submitRequest.setExpiration("1234");
        submitRequest.setCvc("123");
        submitRequest.setInstallment(0);
        submitRequest.setAmount(1000L);

        cancelRequest = new CancelRequest();
        cancelRequest.setId("1234567890");
        cancelRequest.setAmount(1000L);

        paymentRequest = new PaymentRequest();
        paymentRequest.setId("1234567890");
    }

    @Test
    public void postPayment() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.data").exists())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        Response response = objectMapper.readValue(responseBody, Response.class);
        Sent sent = sentRepository.findById(response.getLongId()).orElse(null);

        assertNotNull(sent);
    }
}