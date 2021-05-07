package com.nubiform.payment.api;

import com.nubiform.payment.api.vo.Card;
import com.nubiform.payment.api.vo.SentData;
import com.nubiform.payment.api.vo.SubmitRequest;
import com.nubiform.payment.domain.History;
import com.nubiform.payment.domain.Sent;
import com.nubiform.payment.repository.HistoryRepository;
import com.nubiform.payment.repository.SentRepository;
import com.nubiform.payment.security.Encryption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class PaymentService {

    private final HistoryRepository historyRepository;
    private final SentRepository sentRepository;

    private final ModelMapper modelMapper;
    private final Encryption encryption;

    public Sent submit(SubmitRequest submitRequest) throws Exception {
        Card card = modelMapper.map(submitRequest, Card.class);
        encryption.encrypt(card.toData());

        History history = History.builder()
                .type("PAYMENT")
                .card(encryption.encrypt(card.toData()))
                .amount(submitRequest.getAmount())
                .vat(submitRequest.getVat())
                .build();
        History newHistory = historyRepository.save(history);

        SentData data = modelMapper.map(submitRequest, SentData.class);
        data.setType(newHistory.getType());
        data.setId(newHistory.getId());
        data.setEncryptedCard(newHistory.getCard());

        Sent sent = Sent.builder()
                .id(newHistory.getId())
                .data(data.toString())
                .build();
        Sent newSent = sentRepository.save(sent);

        return newSent;
    }
}
