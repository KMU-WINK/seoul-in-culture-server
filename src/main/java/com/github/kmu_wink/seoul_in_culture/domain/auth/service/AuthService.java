package com.github.kmu_wink.seoul_in_culture.domain.auth.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.github.kmu_wink.seoul_in_culture.common.auth.JwtUtil;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.LoginRequest;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.LoginResponse;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.MyInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.auth.schema.RefreshToken;
import com.github.kmu_wink.seoul_in_culture.domain.auth.exception.AuthenticationFailException;
import com.github.kmu_wink.seoul_in_culture.domain.auth.exception.InvalidRefreshTokenException;
import com.github.kmu_wink.seoul_in_culture.domain.auth.repository.RefreshTokenRedisRepository;
import com.github.kmu_wink.seoul_in_culture.domain.auth.util.KakaoApi;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import com.github.kmu_wink.seoul_in_culture.domain.user.repository.UserRepository;

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

        long kakaoUserInfo = kakaoApi.getKakaoInfo(dto.token()).orElseThrow(AuthenticationFailException::new);

        User user = userRepository.save(
            userRepository.findByKakao(kakaoUserInfo).orElseGet(() ->
                User.builder()
                    .kakao(kakaoUserInfo)
                    .nickname(generateRandomNickname())
                    .email("")
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

    public LoginResponse refreshToken(@Valid LoginRequest request) {

        RefreshToken token = refreshTokenRedisRepository.findByToken(request.token()).orElseThrow(InvalidRefreshTokenException::new);
        refreshTokenRedisRepository.delete(token);

        User user = userRepository.findById(token.userId()).orElseThrow(AuthenticationFailException::new);

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    public MyInfoResponse myInfo(User user) {

        return MyInfoResponse.builder()
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
