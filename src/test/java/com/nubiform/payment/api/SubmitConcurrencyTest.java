package com.nubiform.payment.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubiform.payment.controller.PaymentController;
import com.nubiform.payment.domain.Sent;
import com.nubiform.payment.repository.BalanceRepository;
import com.nubiform.payment.repository.HistoryRepository;
import com.nubiform.payment.repository.SentRepository;
import com.nubiform.payment.vo.SubmitRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class SubmitConcurrencyTest {

    public static final int N_THREADS = 1000;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BalanceRepository balanceRepository;

    @Autowired
    HistoryRepository historyRepository;

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

    @DisplayName("결제 : 하나의 카드번호로 동시에 결제를 할 수 없습니다.")
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        assertion();
    }

    private void assertion() {
        Map<Long, LocalDateTime> sent = sentRepository.findAll().stream()
                .collect(Collectors.toMap(Sent::getId, Sent::getLastModifiedDate));

        historyRepository.findAll(Sort.by(Sort.Direction.ASC, "createdDate")).stream()
                .reduce((before, after) -> {
                    // 이전 트랜잭션의 종료시간이 다음 트랜잭션의 시작시간보다 작아야 한다.
                    // a의 종료시간, b의 시작시간
                    assertTrue(sent.get(before.getId()).isBefore(after.getCreatedDate()));
                    return after;
                });
    }
}