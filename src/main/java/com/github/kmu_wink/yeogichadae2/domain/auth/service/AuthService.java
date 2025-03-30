package com.github.kmu_wink.yeogichadae2.domain.auth.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.github.kmu_wink.yeogichadae2.common.auth.JwtUtil;
import com.github.kmu_wink.yeogichadae2.common.exception.AuthenticationFailException;
import com.github.kmu_wink.yeogichadae2.common.exception.InvalidRefreshTokenException;
import com.github.kmu_wink.yeogichadae2.common.exception.KakaoAccessTokenException;
import com.github.kmu_wink.yeogichadae2.common.property.KakaoProperty;
import com.github.kmu_wink.yeogichadae2.domain.auth.dto.LoginRequest;
import com.github.kmu_wink.yeogichadae2.domain.auth.dto.LoginResponse;
import com.github.kmu_wink.yeogichadae2.domain.auth.dto.MyInfoResponse;
import com.github.kmu_wink.yeogichadae2.domain.auth.entity.RefreshToken;
import com.github.kmu_wink.yeogichadae2.domain.auth.repository.RefreshTokenRedisRepository;
import com.github.kmu_wink.yeogichadae2.domain.user.entity.User;
import com.github.kmu_wink.yeogichadae2.domain.user.repository.UserRepository;

import jakarta.validation.Valid;
import kong.unirest.core.ContentType;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestInstance;
import kong.unirest.core.json.JSONObject;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    private final KakaoProperty kakaoProperty;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        String kakaoAccessToken = getKakaoAccessToken(request.token());
        long kakaoUserInfo = getKakaoUserInfo(kakaoAccessToken);

        User user = userRepository.findByKakao(kakaoUserInfo).orElseGet(() ->
            User.builder()
                .kakao(kakaoUserInfo)
                .nickname(generateRandomNickname())
                .mannerScore(36.5f)
                .build());

        user = userRepository.saveAndFlush(user);

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

    private String getKakaoAccessToken(String token) {
        try (UnirestInstance instance = Unirest.spawnInstance()) {
            JSONObject response = instance.post("https://kauth.kakao.com/oauth/token")
                    .contentType(ContentType.APPLICATION_FORM_URLENCODED)
                    .field("grant_type", "authorization_code")
                    .field("client_id", kakaoProperty.getClientId())
                    .field("client_secret", kakaoProperty.getClientSecret())
                    .field("redirect_url", kakaoProperty.getRedirectUrl())
                    .field("code", token)
                    .asJson()
                    .getBody()
                    .getObject();

            if (response.has("error_code")) throw new KakaoAccessTokenException(response.getString("error_code"));
            return response.getString("access_token");
        }
    }

    private long getKakaoUserInfo(String accessToken) {
        try (UnirestInstance instance = Unirest.spawnInstance()) {

            return instance.get("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .asJson()
                .getBody()
                .getObject()
                .getLong("id");
        }
    }

    private String generateRandomNickname() {

        String nickname;

        do {
            nickname = UUID.randomUUID().toString().split("-")[0];
        } while (userRepository.existsByNickname(nickname));

        return nickname;
    }
}
