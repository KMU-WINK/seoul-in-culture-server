package com.github.kmu_wink.seoul_in_culture.domain.auth.service;

import static com.github.kmu_wink.seoul_in_culture.domain.auth.exception.AuthExceptions.*;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.github.kmu_wink.seoul_in_culture.common.security.jwt.JwtUtil;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.internal.KakaoUser;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.request.LoginRequest;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.response.GetMyTokenInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.response.LoginResponse;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.response.RefreshTokenResponse;
import com.github.kmu_wink.seoul_in_culture.domain.auth.exception.AuthException;
import com.github.kmu_wink.seoul_in_culture.domain.auth.repository.RefreshTokenRedisRepository;
import com.github.kmu_wink.seoul_in_culture.domain.auth.schema.RefreshToken;
import com.github.kmu_wink.seoul_in_culture.domain.auth.util.KakaoApi;
import com.github.kmu_wink.seoul_in_culture.domain.user.repository.UserRepository;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    private final KakaoApi kakaoApi;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest dto) {

        KakaoUser kakaoUser = kakaoApi.getKakaoUser(dto.token())
            .orElseThrow(() -> AuthException.of(INVALID_KAKAO_TOKEN));

        User user = userRepository.save(
            userRepository.findByKakao(kakaoUser.id()).orElseGet(() ->
                User.builder()
                    .kakao(kakaoUser.id())
                    .nickname(generateRandomNickname())
                    .email(kakaoUser.email())
                    .experience(0)
                    .meetingOpen(true)
                    .build()));

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    public RefreshTokenResponse refreshToken(@Valid LoginRequest request) {

        RefreshToken token = refreshTokenRedisRepository.findByToken(request.token())
            .orElseThrow(() -> AuthException.of(INVALID_REFRESH_TOKEN));

        refreshTokenRedisRepository.delete(token);

        User user = userRepository.findById(token.userId()).orElseThrow(() -> AuthException.of(FAIL_AUTHENTICATION));

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return RefreshTokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    public GetMyTokenInfoResponse getMyTokenInfo(User user) {

        return GetMyTokenInfoResponse.builder()
            .user(user)
            .build();
    }

    private String generateRandomNickname() {

        String nickname;

        do {
            nickname = UUID.randomUUID().toString().split("-")[0];
        } while (userRepository.existsByNickname(nickname));

        return nickname;
    }
}
