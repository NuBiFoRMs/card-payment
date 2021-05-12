package com.nubiform.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubiform.payment.domain.Balance;
import com.nubiform.payment.repository.BalanceRepository;
import com.nubiform.payment.vo.CancelRequest;
import com.nubiform.payment.vo.ErrorResponse;
import com.nubiform.payment.vo.SubmitRequest;
import com.nubiform.payment.vo.TestResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class Tests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BalanceRepository balanceRepository;

    @DisplayName("Test Case 1")
    @Test
    public void test1() throws Exception {
        SubmitRequest submitRequest = new SubmitRequest();
        submitRequest.setCard("1234123412341234");
        submitRequest.setExpiration("1234");
        submitRequest.setCvc("123");
        submitRequest.setInstallment(0);
        submitRequest.setAmount(11000L);
        submitRequest.setVat(1000L);

        String submitResult = submit(submitRequest, 11000L, 1000L);

        String id = objectMapper.readValue(submitResult, TestResponse.class).getStringId();

        cancel(CancelRequest.builder().id(id).amount(1100L).vat(100L).build(),
                9900L, 900L);

        cancel(CancelRequest.builder().id(id).amount(3300L).build(),
                6600L, 600L);

        cancel(CancelRequest.builder().id(id).amount(7000L).build(),
                1001,
                6600L, 600L);

        cancel(CancelRequest.builder().id(id).amount(6600L).vat(700L).build(),
                1001,
                6600L, 600L);

        cancel(CancelRequest.builder().id(id).amount(6600L).vat(600L).build(),
                0L, 0L);

        cancel(CancelRequest.builder().id(id).amount(100L).build(),
                1003,
                0L, 0L);
    }

    @DisplayName("Test Case 2")
    @Test
    public void test2() throws Exception {
        SubmitRequest submitRequest = new SubmitRequest();
        submitRequest.setCard("1234123412341234");
        submitRequest.setExpiration("1234");
        submitRequest.setCvc("123");
        submitRequest.setInstallment(0);
        submitRequest.setAmount(20000L);
        submitRequest.setVat(909L);

        String submitResult = submit(submitRequest, 20000L, 909L);

        String id = objectMapper.readValue(submitResult, TestResponse.class).getStringId();

        cancel(CancelRequest.builder().id(id).amount(10000L).vat(0L).build(),
                10000L, 909L);

        cancel(CancelRequest.builder().id(id).amount(10000L).vat(0L).build(),
                1001,
                10000L, 909L);

        cancel(CancelRequest.builder().id(id).amount(10000L).vat(909L).build(),
                0L, 0L);
    }

    @DisplayName("Test Case 3")
    @Test
    public void test3() throws Exception {
        SubmitRequest submitRequest = new SubmitRequest();
        submitRequest.setCard("1234123412341234");
        submitRequest.setExpiration("1234");
        submitRequest.setCvc("123");
        submitRequest.setInstallment(0);
        submitRequest.setAmount(20000L);

        String submitResult = submit(submitRequest, 20000L, 1818L);

        String id = objectMapper.readValue(submitResult, TestResponse.class).getStringId();

        cancel(CancelRequest.builder().id(id).amount(10000L).vat(1000L).build(),
                10000L, 818L);

        cancel(CancelRequest.builder().id(id).amount(10000L).vat(909L).build(),
                1001,
                10000L, 818L);

        cancel(CancelRequest.builder().id(id).amount(10000L).vat(818L).build(),
                0L, 0L);
    }

    private String submit(SubmitRequest submitRequest, Long expectedAmount, Long expectedVat) throws Exception {
        return submit(submitRequest, null, expectedAmount, expectedVat);
    }

    private String submit(SubmitRequest submitRequest, Integer expectedErrorCode, Long expectedAmount, Long expectedVat) throws Exception {
        String result = mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        TestResponse response = objectMapper.readValue(result, TestResponse.class);

        assertResponse(expectedErrorCode, response.getLongId(), expectedAmount, expectedVat, result);

        return result;
    }

    private String cancel(CancelRequest cancelRequest, Long expectedAmount, Long expectedVat) throws Exception {
        return cancel(cancelRequest, null, expectedAmount, expectedVat);
    }

    private String cancel(CancelRequest cancelRequest, Integer expectedErrorCode, Long expectedAmount, Long expectedVat) throws Exception {
        String result = mockMvc.perform(delete("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cancelRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertResponse(expectedErrorCode, cancelRequest.getLongId(), expectedAmount, expectedVat, result);

        return result;
    }

    private void assertResponse(Integer expectedErrorCode, Long id, Long expectedAmount, Long expectedVat, String result) throws Exception {
        if (expectedErrorCode != null) {
            ErrorResponse response = objectMapper.readValue(result, ErrorResponse.class);

            assertEquals(expectedErrorCode, response.getCode());
        }

        Balance balance = balanceRepository.findById(id).orElse(null);

        System.out.println(balance);

        assertNotNull(balance);
        assertEquals(expectedAmount, balance.getRemainAmount(), "assert amount");
        assertEquals(expectedVat, balance.getRemainVat(), "assert vat");
    }
}