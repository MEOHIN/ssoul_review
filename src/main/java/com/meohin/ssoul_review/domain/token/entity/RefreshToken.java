package com.meohin.ssoul_review.domain.token.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId; // 사용자의 고유 ID (예: 회원 ID)

    @Column(name = "token_value", nullable = false, unique = true, length = 500)
    private String tokenValue; // 리프레시 토큰 값

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate; // 토큰 만료일시

    public void updateTokenValue(String newTokenValue, LocalDateTime newExpirationDate) {
        this.tokenValue = newTokenValue;
        this.expirationDate = newExpirationDate;
    }
}
