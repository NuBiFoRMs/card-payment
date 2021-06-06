package com.nubiform.payment.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubiform.payment.controller.PaymentController;
import com.nubiform.payment.repository.SentRepository;
import com.nubiform.payment.service.PaymentService;
import com.nubiform.payment.vo.Id;
import com.nubiform.payment.vo.PaymentRequest;
import com.nubiform.payment.vo.PaymentResponse;
import com.nubiform.payment.vo.SubmitRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

        Long id = Id.convert(paymentService.submit(submitRequest).getId());

        paymentRequest = new PaymentRequest();
        paymentRequest.setId(Id.convert(id));
    }

    @Test
    public void getPayment() throws Exception {
        mockMvc.perform(get(PaymentController.API_V1_PAYMENT_URI)
                .param("id", paymentRequest.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.card").value(submitRequest.getCard().replaceAll(PaymentResponse.MASK_REGEX, PaymentResponse.MASK)))
                .andExpect(jsonPath("$.totalAmount").value(is(submitRequest.getAmount()), Long.class));
    }
}