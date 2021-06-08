package com.nubiform.payment.service;

import com.nubiform.payment.config.ErrorCode;
import com.nubiform.payment.config.PaymentException;
import com.nubiform.payment.config.PaymentType;
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
import com.nubiform.payment.vo.id.PaymentId;
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

    public static final double VAT_RATE = 11D;

    private final BalanceRepository balanceRepository;
    private final CardLockRepository cardLockRepository;
    private final HistoryRepository historyRepository;
    private final SentRepository sentRepository;

    private final Encryption encryption;
    private final ModelMapper modelMapper;

    public PaymentResponse<PaymentPayload> submit(SubmitRequest submitRequest) throws Exception {
        if (submitRequest.getVat() == null) submitRequest.setVat(calculateVat(submitRequest.getAmount()));
        else if (submitRequest.getVat() > submitRequest.getAmount())
            throw new PaymentException(ErrorCode.VatIsNotGreaterThanAmount);

        Card card = modelMapper.map(submitRequest, Card.class);
        String encryptedCard = encryption.encrypt(card.toData());

        getLock(encryptedCard);

        History history = History.builder()
                .type(PaymentType.PAYMENT)
                .card(encryptedCard)
                .installment(submitRequest.getInstallment())
                .amount(submitRequest.getAmount())
                .vat(submitRequest.getVat())
                .build();
        historyRepository.save(history);

        Balance balance = modelMapper.map(history, Balance.class);
        balance.setStatus(history.getType());
        balance.setRemainAmount(history.getAmount());
        balance.setRemainVat(history.getVat());
        balanceRepository.save(balance);

        // mapping history - balance
        history.setBalance(balance);
        historyRepository.save(history);

        PaymentPayload paymentPayload = PaymentPayload.builder()
                .type(history.getType())
                .id(Id.convert(history.getId()))
                .installment(history.getInstallment())
                .amount(history.getAmount())
                .vat(history.getVat())
                .encryptedCard(history.getCard())
                .build();
        modelMapper.map(card, paymentPayload);

        sendPaymentPayload(paymentPayload);

        PaymentResponse<PaymentPayload> paymentResponse = new PaymentResponse<>();
        paymentResponse.setId(history.getId());
        paymentResponse.setData(paymentPayload);

        return paymentResponse;
    }

    public PaymentResponse<PaymentPayload> cancel(PaymentId paymentId, CancelRequest cancelRequest) throws Exception {
        Balance balance = balanceRepository.findById(paymentId.value())
                .orElseThrow(() -> new PaymentException(ErrorCode.NoDataFound));

        if (balance.isCanceled()) throw new PaymentException(ErrorCode.PaymentIsAlreadyCanceled);

        if (cancelRequest.getVat() == null) {
            long vat = calculateVat(cancelRequest.getAmount());
            if (balance.getRemainAmount() - cancelRequest.getAmount() == 0 && balance.getRemainVat() < vat)
                cancelRequest.setVat(balance.getRemainVat());
            else
                cancelRequest.setVat(vat);
        } else if (cancelRequest.getVat() > cancelRequest.getAmount())
            throw new PaymentException(ErrorCode.VatIsNotGreaterThanAmount);

        if (!balance.cancel(cancelRequest.getAmount(), cancelRequest.getVat()))
            throw new PaymentException(ErrorCode.NotEnoughAmountOrVat);

        History history = History.builder()
                .type(PaymentType.CANCEL)
                .card(balance.getCard())
                .installment(0)
                .amount(cancelRequest.getAmount())
                .vat(cancelRequest.getVat())
                .balance(balance)
                .build();
        historyRepository.save(history);

        Card card = new Card(encryption.decrypt(history.getCard()));

        PaymentPayload paymentPayload = PaymentPayload.builder()
                .type(history.getType())
                .id(Id.convert(history.getId()))
                .installment(history.getInstallment())
                .amount(history.getAmount())
                .vat(history.getVat())
                .originId(Id.convert(history.getBalance().getId()))
                .encryptedCard(history.getCard())
                .build();
        modelMapper.map(card, paymentPayload);

        sendPaymentPayload(paymentPayload);

        PaymentResponse<PaymentPayload> paymentResponse = new PaymentResponse<>();
        paymentResponse.setId(history.getId());
        paymentResponse.setData(paymentPayload);

        return paymentResponse;
    }

    private long calculateVat(long amount) {
        return Math.round(amount / VAT_RATE);
    }

    public PaymentResponse payment(PaymentId paymentId) throws Exception {
        History history = historyRepository.findById(paymentId.value())
                .orElseThrow(() -> new PaymentException(ErrorCode.NoDataFound));

        Payment payment = modelMapper.map(history, Payment.class);
        Card card = new Card(encryption.decrypt(history.getCard()));
        modelMapper.map(card, payment);

        Balance balance = history.getBalance();
        payment.setOriginId(balance.getId());
        payment.setTotalAmount(balance.getAmount());
        payment.setTotalVat(balance.getVat());
        payment.setRemainAmount(balance.getRemainAmount());
        payment.setRemainVat(balance.getRemainVat());

        PaymentResponse<Payment> paymentResponse = new PaymentResponse<>();
        paymentResponse.setId(balance.getId());
        paymentResponse.setData(payment);

        return paymentResponse;
    }

    private Sent sendPaymentPayload(PaymentPayload paymentPayload) {
        log.debug("paymentPayload: {}", paymentPayload);
        Sent sent = Sent.builder()
                .id(Id.convert(paymentPayload.getId()))
                .data(paymentPayload.serialize())
                .build();
        sentRepository.save(sent);
        return sent;
    }

    private void getLock(String encryptedCard) {
        CardLock cardLock = cardLockRepository.findById(encryptedCard)
                .orElse(CardLock.builder()
                        .card(encryptedCard)
                        .build());
        cardLock.generateLockId();
        cardLockRepository.save(cardLock);
    }
}
