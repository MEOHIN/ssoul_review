package com.meohin.ssoul_review.global.security;

import com.meohin.ssoul_review.domain.user.model.User;
import com.meohin.ssoul_review.global.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {
            try {
                Authentication authentication = createAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Authentication successful for user: {}", authentication.getName());
            } catch (Exception e) {
                log.error("Cannot set user authentication: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        } else {
            log.trace("No valid JWT token found in request URI: {}", request.getRequestURI());
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private Authentication createAuthentication(String token) {
        Claims claims = jwtUtil.getPayload(token);
        long userId = claims.get("userId", Long.class);

        // User 엔티티를 직접 사용하는 대신, UserDetails 객체를 생성하여 사용합니다.
        // 여기서는 User 엔티티가 있다고 가정하고 CustomUserDetails를 만듭니다.
        // 실제로는 DB에서 사용자 정보를 조회하는 로직이 필요할 수 있습니다.
        User user = User.builder()
                .id(userId)
                .email(claims.getSubject())
                // .role(...) // claims에서 역할 정보를 가져와 설정
                .build();
        
        CustomUserDetails userDetails = new CustomUserDetails(user);
        
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
