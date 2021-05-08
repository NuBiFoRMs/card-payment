package com.nubiform.payment.api;

import com.nubiform.payment.domain.Sent;
import com.nubiform.payment.repository.SentRepository;
import com.nubiform.payment.service.PaymentService;
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
class PaymentServiceTest {

    public static final int N_THREADS = 50;

    @Autowired
    PaymentService paymentService;

    @Autowired
    SentRepository sentRepository;

    SubmitRequest submitRequest;

    @BeforeEach
    void setUp() {
        submitRequest = new SubmitRequest();
        submitRequest.setCard("1234567890123456");
        submitRequest.setExpiration("1234");
        submitRequest.setCvc("123");
        submitRequest.setInstallment(0);
        submitRequest.setAmount(10000L);
    }

    @Test
    public void submitConcurrencyTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
        CountDownLatch countDownLatch = new CountDownLatch(N_THREADS);

        for (int i = 0; i < N_THREADS; i++) {
            executorService.execute(() -> {
                try {
                    paymentService.submit(submitRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        sentRepository.findAll().stream()
                .map(Sent::toString)
                .forEach(System.out::println);
    }
}