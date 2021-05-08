package com.nubiform.payment.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubiform.payment.vo.CancelRequest;
import com.nubiform.payment.vo.PaymentRequest;
import com.nubiform.payment.vo.SubmitRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerInvalidParamTest {

    @Autowired
    MockMvc mockMvc;

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
        submitRequest.setAmount(10000L);

        cancelRequest = new CancelRequest();
        cancelRequest.setId(1L);
        cancelRequest.setAmount(1000L);

        paymentRequest = new PaymentRequest();
        paymentRequest.setId(1L);
    }

    @Test
    public void postPaymentWrongCard() throws Exception {
        submitRequest.setCard("12345678");

        mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postPaymentWrongExpiration() throws Exception {
        submitRequest.setExpiration("123");

        mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postPaymentWrongCvc() throws Exception {
        submitRequest.setCvc("12");

        mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postPaymentWrongInstallment() throws Exception {
        submitRequest.setInstallment(13);

        mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postPaymentWrongAmountMin() throws Exception {
        submitRequest.setAmount(99L);

        mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postPaymentWrongAmountMax() throws Exception {
        submitRequest.setAmount(1000000001L);

        mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postPaymentWrongVat() throws Exception {
        submitRequest.setVat(-1L);

        mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void delPaymentWrongId() throws Exception {
        cancelRequest.setId("1234567890");

        mockMvc.perform(delete("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cancelRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void delPaymentWrongAmount() throws Exception {
        cancelRequest.setAmount(1000000001L);

        mockMvc.perform(delete("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cancelRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void delPaymentWrongVat() throws Exception {
        cancelRequest.setVat(-1L);

        mockMvc.perform(delete("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cancelRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getPaymentWrongId() throws Exception {
        paymentRequest.setId("1234567890");

        mockMvc.perform(get("/api/v1/payment")
                .param("id", paymentRequest.getId()))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}