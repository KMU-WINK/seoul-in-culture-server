package com.github.kmu_wink.seoul_in_culture.domain.auth.util;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.github.kmu_wink.seoul_in_culture.common.property.KakaoProperty;
import com.github.kmu_wink.seoul_in_culture.domain.auth.dto.internal.KakaoUser;
import com.github.kmu_wink.seoul_in_culture.domain.auth.exception.AuthException;
import com.github.kmu_wink.seoul_in_culture.domain.auth.exception.AuthExceptions;

import kong.unirest.core.ContentType;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestInstance;
import kong.unirest.core.json.JSONObject;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoApi {

	private final KakaoProperty kakaoProperty;

	public Optional<KakaoUser> getKakaoUser(String token) {

		Optional<String> accessToken;

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

			if (response.has("error_code")) throw AuthException.of(AuthExceptions.INVALID_KAKAO_TOKEN);

			accessToken = Optional.ofNullable(response.getString("access_token"));
		}

		if (accessToken.isEmpty()) return Optional.empty();

		try (UnirestInstance instance = Unirest.spawnInstance()) {

			JSONObject response = instance.get("https://kapi.kakao.com/v2/user/me")
				.header("Authorization", "Bearer " + accessToken.get())
				.asJson()
				.getBody()
				.getObject();

			return Optional.of(
				KakaoUser.builder()
					.id(response.getLong("id"))
					.email(response.getJSONObject("kakao_account").getString("email"))
					.build());
		}
	}
}
