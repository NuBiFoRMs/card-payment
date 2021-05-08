package com.nubiform.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubiform.payment.domain.Balance;
import com.nubiform.payment.repository.BalanceRepository;
import com.nubiform.payment.vo.CancelRequest;
import com.nubiform.payment.vo.Response;
import com.nubiform.payment.vo.SubmitRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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

    @Test
    public void test1() throws Exception {
        SubmitRequest submitRequest = new SubmitRequest();
        submitRequest.setCard("1234123412341234");
        submitRequest.setExpiration("1234");
        submitRequest.setCvc("123");
        submitRequest.setInstallment(0);
        submitRequest.setAmount(11000L);
        submitRequest.setVat(1000L);

        List<String> resultList = new ArrayList<>();

        String submit = submit(submitRequest);
        Response response = objectMapper.readValue(submit, Response.class);
        resultList.add(submit);

        balanceRepository.findById(response.getLongId()).stream().map(Balance::toString).forEach(System.out::println);
        resultList.add(cancel(CancelRequest.builder().id(response.getId()).amount(1100L).vat(100L).build()));
        balanceRepository.findById(response.getLongId()).stream().map(Balance::toString).forEach(System.out::println);
        resultList.add(cancel(CancelRequest.builder().id(response.getId()).amount(3300L).build()));
        balanceRepository.findById(response.getLongId()).stream().map(Balance::toString).forEach(System.out::println);
        resultList.add(cancel(CancelRequest.builder().id(response.getId()).amount(7000L).build()));
        balanceRepository.findById(response.getLongId()).stream().map(Balance::toString).forEach(System.out::println);
        resultList.add(cancel(CancelRequest.builder().id(response.getId()).amount(6600L).vat(700L).build()));
        balanceRepository.findById(response.getLongId()).stream().map(Balance::toString).forEach(System.out::println);
        resultList.add(cancel(CancelRequest.builder().id(response.getId()).amount(6600L).vat(600L).build()));
        balanceRepository.findById(response.getLongId()).stream().map(Balance::toString).forEach(System.out::println);
        resultList.add(cancel(CancelRequest.builder().id(response.getId()).amount(100L).build()));
        balanceRepository.findById(response.getLongId()).stream().map(Balance::toString).forEach(System.out::println);

        resultList.stream()
                .forEach(System.out::println);
    }

    @Test
    public void test2() throws Exception {
        SubmitRequest submitRequest = new SubmitRequest();
        submitRequest.setCard("1234123412341234");
        submitRequest.setExpiration("1234");
        submitRequest.setCvc("123");
        submitRequest.setInstallment(0);
        submitRequest.setAmount(20000L);
        submitRequest.setVat(909L);

        List<String> resultList = new ArrayList<>();

        String submit = submit(submitRequest);
        Response response = objectMapper.readValue(submit, Response.class);
        resultList.add(submit);

        balanceRepository.findById(response.getLongId()).stream().map(Balance::toString).forEach(System.out::println);
        resultList.add(cancel(CancelRequest.builder().id(response.getId()).amount(10000L).vat(0L).build()));
        balanceRepository.findById(response.getLongId()).stream().map(Balance::toString).forEach(System.out::println);
        resultList.add(cancel(CancelRequest.builder().id(response.getId()).amount(10000L).vat(0L).build()));
        balanceRepository.findById(response.getLongId()).stream().map(Balance::toString).forEach(System.out::println);
        resultList.add(cancel(CancelRequest.builder().id(response.getId()).amount(10000L).vat(909L).build()));
        balanceRepository.findById(response.getLongId()).stream().map(Balance::toString).forEach(System.out::println);

        resultList.stream()
                .forEach(System.out::println);
    }

    @Test
    public void test3() throws Exception {
        SubmitRequest submitRequest = new SubmitRequest();
        submitRequest.setCard("1234123412341234");
        submitRequest.setExpiration("1234");
        submitRequest.setCvc("123");
        submitRequest.setInstallment(0);
        submitRequest.setAmount(20000L);

        List<String> resultList = new ArrayList<>();

        String submit = submit(submitRequest);
        Response response = objectMapper.readValue(submit, Response.class);
        resultList.add(submit);

        balanceRepository.findById(response.getLongId()).stream().map(Balance::toString).forEach(System.out::println);
        resultList.add(cancel(CancelRequest.builder().id(response.getId()).amount(10000L).vat(1000L).build()));
        balanceRepository.findById(response.getLongId()).stream().map(Balance::toString).forEach(System.out::println);
        resultList.add(cancel(CancelRequest.builder().id(response.getId()).amount(10000L).vat(909L).build()));
        balanceRepository.findById(response.getLongId()).stream().map(Balance::toString).forEach(System.out::println);
        resultList.add(cancel(CancelRequest.builder().id(response.getId()).amount(10000L).build()));
        balanceRepository.findById(response.getLongId()).stream().map(Balance::toString).forEach(System.out::println);

        resultList.stream()
                .forEach(System.out::println);
    }

    private String submit(SubmitRequest submitRequest) throws Exception {
        return mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitRequest)))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private String cancel(CancelRequest cancelRequest) throws Exception {
        return mockMvc.perform(delete("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cancelRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}