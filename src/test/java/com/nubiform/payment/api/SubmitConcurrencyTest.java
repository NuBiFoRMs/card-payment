package com.nubiform.payment.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubiform.payment.controller.PaymentController;
import com.nubiform.payment.repository.SentRepository;
import com.nubiform.payment.vo.SubmitRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class SubmitConcurrencyTest {

    public static final int N_THREADS = 100;

    @Autowired
    MockMvc mockMvc;

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
        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
        CountDownLatch countDownLatch = new CountDownLatch(N_THREADS);

        for (int i = 0; i < N_THREADS; i++) {
            executorService.execute(() -> {
                try {
                    MvcResult mvcResult = mockMvc.perform(post(PaymentController.API_V1_PAYMENT_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(submitRequest)))
                            .andReturn();
                    System.out.println(mvcResult.getResponse().getContentAsString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
    }
}