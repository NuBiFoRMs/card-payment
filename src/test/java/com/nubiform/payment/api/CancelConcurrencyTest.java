package com.nubiform.payment.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubiform.payment.config.PaymentType;
import com.nubiform.payment.controller.PaymentController;
import com.nubiform.payment.domain.Balance;
import com.nubiform.payment.domain.History;
import com.nubiform.payment.repository.BalanceRepository;
import com.nubiform.payment.repository.HistoryRepository;
import com.nubiform.payment.repository.SentRepository;
import com.nubiform.payment.service.PaymentService;
import com.nubiform.payment.vo.CancelRequest;
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

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CancelConcurrencyTest {

    public static final int N_THREADS = 1000;

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
        cancelRequest.setId(id);
        cancelRequest.setAmount(10000L);
    }

    @Test
    public void delPayment() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
        CountDownLatch countDownLatch = new CountDownLatch(N_THREADS);

        for (int i = 0; i < N_THREADS; i++) {
            executorService.execute(() -> {
                try {
                    MvcResult mvcResult = mockMvc.perform(delete(PaymentController.API_V1_PAYMENT_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(cancelRequest)))
                            .andReturn();
                    System.out.println(mvcResult.getResponse().getContentAsString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        validation(cancelRequest.getLongId());
    }

    private void validation(Long id) {
        History history = historyRepository.findById(id).orElse(null);
        assertNotNull(history);

        Balance balance = balanceRepository.findById(id).orElse(null);
        assertNotNull(balance);

        List<History> historyListByBalance = historyRepository.findByBalanceAndType(balance, PaymentType.CANCEL);
        long amountSum = historyListByBalance.stream().mapToLong(History::getAmount).sum();
        long vatSum = historyListByBalance.stream().mapToLong(History::getVat).sum();

        assertEquals(history.getAmount(), amountSum);
        assertEquals(history.getVat(), vatSum);
    }
}