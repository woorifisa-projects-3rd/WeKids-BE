package com.wekids.backend.card.controller;

import com.wekids.backend.card.domain.Card;
import com.wekids.backend.card.dto.response.CardResponse;
import com.wekids.backend.card.service.CardService;
import com.wekids.backend.design.domain.Design;
import com.wekids.backend.design.repository.DesignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/design")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final DesignRepository designRepository;

    @GetMapping("/{cardId}")
    public CardResponse getCardDetails(@PathVariable Long cardId) {

        Card card = cardService.findById(cardId).get();
        Design design = designRepository.findByCard(card);

        return new CardResponse(
                design.getColor(),
                design.getCharacter()
        );
    }

}
