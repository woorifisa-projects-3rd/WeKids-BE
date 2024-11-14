package com.wekids.backend.card.dto.response;

import com.wekids.backend.card.dto.result.CardResult;
import com.wekids.backend.design.domain.enums.CharacterType;
import com.wekids.backend.design.domain.enums.ColorType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardResponse {
    private String color;
    private String character;

    public CardResponse(ColorType color, CharacterType character) {
        this.color = color.name();
        this.character = character.name();
    }
}
