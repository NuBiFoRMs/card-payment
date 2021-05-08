package com.nubiform.payment.service;

import com.nubiform.payment.domain.Balance;
import com.nubiform.payment.domain.Sent;
import com.nubiform.payment.repository.BalanceRepository;
import com.nubiform.payment.repository.SentRepository;
import com.nubiform.payment.vo.CancelRequest;
import com.nubiform.payment.vo.SubmitRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ActiveProfiles("test")
@SpringBootTest
class PaymentServiceCancelTest {

    public static final int N_THREADS = 10;

    @Autowired
    PaymentService paymentService;

    @Autowired
    BalanceRepository balanceRepository;

    @Autowired
    SentRepository sentRepository;

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
        cancelRequest.setAmount(1000L);
    }

    @Test
    public void cancelConcurrencyTest() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
        CountDownLatch countDownLatch = new CountDownLatch(N_THREADS);

        for (int i = 0; i < N_THREADS; i++) {
            executorService.execute(() -> {
                try {
                    paymentService.cancel(cancelRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        balanceRepository.findAll().stream()
                .map(Balance::toString)
                .forEach(System.out::println);

        sentRepository.findAll().stream()
                .map(Sent::toString)
                .forEach(System.out::println);
    }
}