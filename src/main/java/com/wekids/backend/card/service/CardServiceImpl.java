package com.wekids.backend.card.service;

import com.wekids.backend.card.domain.Card;
import com.wekids.backend.card.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    @Override
    public Optional<Card> findById(Long cardId) {
        return cardRepository.findById(cardId);
    }
}
