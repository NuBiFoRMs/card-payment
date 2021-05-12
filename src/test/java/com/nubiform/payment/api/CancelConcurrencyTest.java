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
import com.nubiform.payment.vo.Id;
import com.nubiform.payment.vo.SubmitRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    public static final long TOTAL_AMOUNT = 10000L;
    public static final long PARTIAL_AMOUNT = 1000L;

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
        submitRequest.setAmount(TOTAL_AMOUNT);

        Long id = paymentService.submit(submitRequest).getId();

        cancelRequest = new CancelRequest();
        cancelRequest.setId(Id.convert(id));
    }

    @DisplayName("전체취소 : 결제 한 건에 대한 전체취소를 동시에 할 수 없습니다.")
    @Test
    public void delPaymentAllCancellation() throws Exception {
        cancelRequest.setAmount(TOTAL_AMOUNT);

        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
        CountDownLatch countDownLatch = new CountDownLatch(N_THREADS);
        for (int i = 0; i < N_THREADS; i++) {
            executorService.execute(() -> {
                try {
                    MvcResult mvcResult = mockMvc.perform(delete(PaymentController.API_V1_PAYMENT_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(cancelRequest)))
                            .andReturn();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        assertion(cancelRequest.getLongId());
    }

    @DisplayName("부분취소 : 결제 한 건에 대한 부분취소를 동시에 할 수 없습니다.")
    @Test
    public void delPaymentPartialCancellation() throws Exception {
        cancelRequest.setAmount(PARTIAL_AMOUNT);

        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
        CountDownLatch countDownLatch = new CountDownLatch(N_THREADS);
        for (int i = 0; i < N_THREADS; i++) {
            executorService.execute(() -> {
                try {
                    MvcResult mvcResult = mockMvc.perform(delete(PaymentController.API_V1_PAYMENT_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(cancelRequest)))
                            .andReturn();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        assertion(cancelRequest.getLongId());
    }

    private void assertion(Long id) {
        History history = historyRepository.findById(id).orElse(null);
        assertNotNull(history);

        Balance balance = balanceRepository.findById(id).orElse(null);
        assertNotNull(balance);

        List<History> historyListByBalance = historyRepository.findByBalance(balance);

        long amountSum = historyListByBalance.stream()
                .filter(a -> a.getType().equals(PaymentType.CANCEL))
                .mapToLong(History::getAmount)
                .sum();
        long vatSum = historyListByBalance.stream()
                .filter(a -> a.getType().equals(PaymentType.CANCEL))
                .mapToLong(History::getVat)
                .sum();

        assertEquals(balance.getRemainAmount(), history.getAmount() - amountSum);
        assertEquals(balance.getRemainVat(), history.getVat() - vatSum);
    }
}