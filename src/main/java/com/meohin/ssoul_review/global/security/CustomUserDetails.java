package com.meohin.ssoul_review.global.security;

import com.meohin.ssoul_review.domain.user.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 여기서는 간단하게 ROLE_USER 권한을 부여합니다.
        // 실제 애플리케이션에서는 User 엔티티의 역할을 바탕으로 권한을 설정해야 합니다.
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        // JWT 인증에서는 비밀번호를 직접 사용하지 않으므로 null 또는 빈 문자열 반환
        return null;
    }

    @Override
    public String getUsername() {
        // 사용자 고유 식별자 (예: 이메일 또는 ID)
        return user.getEmail(); // User 엔티티에 email 필드가 있다고 가정
    }
    
    public Long getUserId() {
        return user.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
