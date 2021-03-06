package com.nubiform.payment.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubiform.payment.controller.PaymentController;
import com.nubiform.payment.vo.CancelRequest;
import com.nubiform.payment.vo.SubmitRequest;
import com.nubiform.payment.vo.id.PaymentId;
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
class InvalidParamTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    SubmitRequest submitRequest;
    PaymentId paymentId;
    CancelRequest cancelRequest;

    @BeforeEach
    void setUp() {
        submitRequest = new SubmitRequest();
        submitRequest.setCard("1234567890123456");
        submitRequest.setExpiration("1234");
        submitRequest.setCvc("123");
        submitRequest.setInstallment(0);
        submitRequest.setAmount(10000L);

        paymentId = PaymentId.of(1L);

        cancelRequest = new CancelRequest();
        cancelRequest.setAmount(1000L);
    }

    @Test
    public void postPaymentWrongCard() throws Exception {
        submitRequest.setCard("12345678");

        mockMvc.perform(post(PaymentController.API_V1_PAYMENT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postPaymentWrongExpirationLength() throws Exception {
        submitRequest.setExpiration("123");

        mockMvc.perform(post(PaymentController.API_V1_PAYMENT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postPaymentWrongExpirationYYMM() throws Exception {
        submitRequest.setExpiration("9999");

        mockMvc.perform(post(PaymentController.API_V1_PAYMENT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postPaymentWrongCvc() throws Exception {
        submitRequest.setCvc("12");

        mockMvc.perform(post(PaymentController.API_V1_PAYMENT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postPaymentWrongInstallment() throws Exception {
        submitRequest.setInstallment(13);

        mockMvc.perform(post(PaymentController.API_V1_PAYMENT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postPaymentWrongAmountMin() throws Exception {
        submitRequest.setAmount(99L);

        mockMvc.perform(post(PaymentController.API_V1_PAYMENT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postPaymentWrongAmountMax() throws Exception {
        submitRequest.setAmount(1000000001L);

        mockMvc.perform(post(PaymentController.API_V1_PAYMENT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postPaymentWrongVat() throws Exception {
        submitRequest.setVat(-1L);

        mockMvc.perform(post(PaymentController.API_V1_PAYMENT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void delPaymentWrongId() throws Exception {
        mockMvc.perform(delete(PaymentController.API_V1_PAYMENT_URI_WITH_ID, "1234567890")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cancelRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void delPaymentWrongAmountMin() throws Exception {
        cancelRequest.setAmount(0L);

        mockMvc.perform(delete(PaymentController.API_V1_PAYMENT_URI_WITH_ID, paymentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cancelRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void delPaymentWrongAmountMax() throws Exception {
        cancelRequest.setAmount(1000000001L);

        mockMvc.perform(delete(PaymentController.API_V1_PAYMENT_URI_WITH_ID, paymentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cancelRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void delPaymentWrongVat() throws Exception {
        cancelRequest.setVat(-1L);

        mockMvc.perform(delete(PaymentController.API_V1_PAYMENT_URI_WITH_ID, paymentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cancelRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getPaymentWrongId() throws Exception {
        mockMvc.perform(get(PaymentController.API_V1_PAYMENT_URI_WITH_ID, "1234567890"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}