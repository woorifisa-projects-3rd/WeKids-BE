package com.wekids.backend.card.controller;

import com.wekids.backend.card.domain.Card;
import com.wekids.backend.card.service.CardService;
import com.wekids.backend.design.domain.Design;
import com.wekids.backend.design.domain.enums.CharacterType;
import com.wekids.backend.design.domain.enums.ColorType;
import com.wekids.backend.design.repository.DesignRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    @MockBean
    private DesignRepository designRepository;

    @Test
    void 카드_정보_가져오기() throws Exception {
        Long cardId = 1L;
        Card mockCard = new Card();

        Design mockDesign = Design.builder()
                .color(ColorType.PINK1)
                .character(CharacterType.HEARTSPRING)
                .card(mockCard)
                .build();

        when(cardService.findById(cardId)).thenReturn(mockCard);
        when(designRepository.findByCard(mockCard)).thenReturn(mockDesign);

        mockMvc.perform(get("/api/v1/design/{cardId}", cardId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value("PINK1"))
                .andExpect(jsonPath("$.character").value("HEARTSPRING"));
    }
}
