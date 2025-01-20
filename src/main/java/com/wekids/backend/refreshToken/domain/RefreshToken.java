package com.wekids.backend.refreshToken.domain;

import com.wekids.backend.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

    public static RefreshToken of(String token, Member member, Long expirationMilliseconds) {
        return RefreshToken.builder()
                .token(token)
                .member(member)
                .expirationTime(LocalDateTime.now().plusSeconds(expirationMilliseconds / 1000))
                .build();
    }
}