package com.nubiform.payment.service;

import com.nubiform.payment.domain.Balance;
import com.nubiform.payment.domain.CardLock;
import com.nubiform.payment.domain.History;
import com.nubiform.payment.domain.Sent;
import com.nubiform.payment.repository.BalanceRepository;
import com.nubiform.payment.repository.CardLockRepository;
import com.nubiform.payment.repository.HistoryRepository;
import com.nubiform.payment.repository.SentRepository;
import com.nubiform.payment.security.Encryption;
import com.nubiform.payment.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class PaymentService {

    private final BalanceRepository balanceRepository;
    private final CardLockRepository cardLockRepository;
    private final HistoryRepository historyRepository;
    private final SentRepository sentRepository;

    private final Encryption encryption;
    private final ModelMapper modelMapper;

    public Sent submit(SubmitRequest submitRequest) throws Exception {
        Card card = modelMapper.map(submitRequest, Card.class);
        String encryptedCard = encryption.encrypt(card.toData());

        CardLock cardLock = cardLockRepository.findById(encryptedCard)
                .orElse(CardLock.builder()
                        .card(encryptedCard)
                        .build());
        cardLock.generateLockId();
        cardLockRepository.save(cardLock);

        History history = History.builder()
                .type("PAYMENT")
                .card(encryptedCard)
                .installment(submitRequest.getInstallment())
                .amount(submitRequest.getAmount())
                .vat(submitRequest.getVat())
                .build();
        historyRepository.save(history);

        Balance balance = modelMapper.map(history, Balance.class);
        balance.setStatus(history.getType());
        balanceRepository.save(balance);

        SentData data = modelMapper.map(submitRequest, SentData.class);
        data.setType(history.getType());
        data.setId(history.getId());
        data.setEncryptedCard(history.getCard());

        Sent sent = Sent.builder()
                .id(history.getId())
                .data(data.toString())
                .build();
        sentRepository.save(sent);

        return sent;
    }

    public Sent cancel(CancelRequest cancelRequest) throws Exception {
        Balance balance = balanceRepository.findById(cancelRequest.getLongId()).orElse(null);

        if (!balance.cancel(cancelRequest.getAmount(), cancelRequest.getVat()))
            throw new IllegalArgumentException();

        History history = History.builder()
                .type("CANCEL")
                .card(balance.getCard())
                .installment(0)
                .amount(cancelRequest.getAmount())
                .vat(cancelRequest.getVat())
                .originId(balance.getId())
                .build();
        historyRepository.save(history);

        Card card = new Card(encryption.decrypt(history.getCard()));
        SentData data = modelMapper.map(history, SentData.class);
        modelMapper.map(card, data);
        data.setEncryptedCard(history.getCard());

        Sent sent = Sent.builder()
                .id(history.getId())
                .data(data.toString())
                .build();
        sentRepository.save(sent);

        return sent;
    }

    public PaymentResponse payment(PaymentRequest paymentRequest) throws Exception {
        History history = historyRepository.findById(paymentRequest.getLongId()).orElse(null);
        PaymentResponse paymentResponse = modelMapper.map(history, PaymentResponse.class);
        Card card = new Card(encryption.decrypt(history.getCard()));
        modelMapper.map(card, paymentResponse);
        return paymentResponse;
    }
}
