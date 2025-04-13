package com.github.kmu_wink.seoul_in_culture.domain.auth.service;

import com.github.kmu_wink.seoul_in_culture.common.security.jwt.JwtUtil;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.internal.KakaoUser;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.request.LoginRequest;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.response.GetMyTokenInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.response.LoginResponse;
import com.github.kmu_wink.seoul_in_culture.domain.auth.exception.AuthException;
import com.github.kmu_wink.seoul_in_culture.domain.auth.util.KakaoApi;
import com.github.kmu_wink.seoul_in_culture.domain.user.repository.UserRepository;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.github.kmu_wink.seoul_in_culture.domain.auth.exception.AuthExceptions.INVALID_KAKAO_TOKEN;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

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

        return LoginResponse.builder()
                .token(jwtUtil.generateToken(user))
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
