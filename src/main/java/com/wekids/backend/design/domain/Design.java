package com.wekids.backend.design.domain;

import com.wekids.backend.account.domain.Account;
import com.wekids.backend.card.domain.Card;
import com.wekids.backend.design.domain.enums.CharacterType;
import com.wekids.backend.design.domain.enums.ColorType;
import com.wekids.backend.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
public class Design {
    @Id
    private Long memberId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @MapsId
    private Member member;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // 추가
    private ColorType color;

    @Column(name = "`character`", nullable = false)
    @Enumerated(EnumType.STRING) // 추가
    private CharacterType character;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;
}
