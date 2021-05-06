package com.nubiform.payment.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubiform.payment.api.vo.SubmitRequest;
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
class PaymentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    SubmitRequest submitRequest;

    @BeforeEach
    void setUp() {
        submitRequest = new SubmitRequest();
        submitRequest.setCard("1234123412341234");
        submitRequest.setExpiration("1234");
        submitRequest.setCvc("123");
        submitRequest.setInstallment(0);
        submitRequest.setAmount(100);
    }

    @Test
    public void postPayment() throws Exception {
        mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void postPaymentWrongCard() throws Exception {
        submitRequest.setCard("12341234");

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
        submitRequest.setAmount(99);

        mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postPaymentWrongAmountMax() throws Exception {
        submitRequest.setAmount(1000000001);

        mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postPaymentWrongVat() throws Exception {
        submitRequest.setVat(-1);

        mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
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