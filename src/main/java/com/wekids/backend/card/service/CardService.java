package com.wekids.backend.card.service;

import com.wekids.backend.card.domain.Card;

import java.util.Optional;

public interface CardService {
    Optional<Card> findById(Long cardId);
}
