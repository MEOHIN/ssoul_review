package com.meohin.ssoul_review.global.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "custom.jwt")
public class JwtProperties {
    private String secretKey;
    private final AccessToken accessToken = new AccessToken();
    private final RefreshToken refreshToken = new RefreshToken();

    @Getter
    @Setter
    public static class AccessToken {
        private int expirationSeconds;
    }

    @Getter
    @Setter
    public static class RefreshToken {
        private int expirationSeconds;
        private int idleTimeoutHours;
    }
}
