package com.meohin.ssoul_review.domain.token.service;

import com.meohin.ssoul_review.domain.token.entity.RefreshToken;
import com.meohin.ssoul_review.domain.token.repository.RefreshTokenRepository;
import com.meohin.ssoul_review.global.jwt.JwtProperties;
import com.meohin.ssoul_review.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;

    /**
     * 사용자 ID에 해당하는 리프레시 토큰을 발급(또는 갱신)합니다.
     *
     * @param userId 사용자 ID
     * @return 발급된 리프레시 토큰 문자열
     */
    public String issueRefreshToken(Long userId) {
        String tokenValue = jwtUtil.generateRefreshToken();
        int expirationSeconds = jwtProperties.getRefreshToken().getExpirationSeconds();
        LocalDateTime expirationDate = LocalDateTime.now().plusSeconds(expirationSeconds);

        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUserId(userId);

        if (optionalRefreshToken.isPresent()) {
            RefreshToken refreshToken = optionalRefreshToken.get();
            refreshToken.updateTokenValue(tokenValue, expirationDate);
            refreshTokenRepository.save(refreshToken);
        } else {
            RefreshToken newRefreshToken = RefreshToken.builder()
                    .userId(userId)
                    .tokenValue(tokenValue)
                    .expirationDate(expirationDate)
                    .build();
            refreshTokenRepository.save(newRefreshToken);
        }

        return tokenValue;
    }

    /**
     * 토큰 값으로 리프레시 토큰을 조회합니다.
     *
     * @param tokenValue 토큰 값
     * @return RefreshToken 객체 (Optional)
     */
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findByTokenValue(String tokenValue) {
        return refreshTokenRepository.findByTokenValue(tokenValue);
    }

    /**
     * 사용자 ID로 리프레시 토큰을 삭제합니다. (로그아웃 시 사용)
     *
     * @param userId 사용자 ID
     */
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
