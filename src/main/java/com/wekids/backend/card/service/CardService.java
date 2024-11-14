package com.wekids.backend.card.service;
import com.wekids.backend.card.domain.Card;

public interface CardService {
    Card findById(Long cardId);
}
