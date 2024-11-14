package com.wekids.backend.card.dto.result;

import com.wekids.backend.design.domain.enums.CharacterType;
import com.wekids.backend.design.domain.enums.ColorType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardResult {
    private String color;
    private String character;

    public CardResult(ColorType color, CharacterType character) {
        this.color = color.name();
        this.character = character.name();
    }
}
