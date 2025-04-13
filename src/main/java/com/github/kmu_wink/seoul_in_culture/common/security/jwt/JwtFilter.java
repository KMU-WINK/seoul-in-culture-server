package com.github.kmu_wink.seoul_in_culture.common.security.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kmu_wink.seoul_in_culture.common.api.ApiException;
import com.github.kmu_wink.seoul_in_culture.common.api.ApiResponse;
import com.github.kmu_wink.seoul_in_culture.common.security.authentication.UserAuthentication;
import com.github.kmu_wink.seoul_in_culture.domain.auth.exception.AuthException;
import com.github.kmu_wink.seoul_in_culture.domain.user.repository.UserRepository;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.github.kmu_wink.seoul_in_culture.domain.auth.exception.AuthExceptions.EXPIRED_TOKEN;
import static com.github.kmu_wink.seoul_in_culture.domain.auth.exception.AuthExceptions.FAIL_AUTHENTICATION;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository repository;

    @Override
    public void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {

        String accessToken = extractToken(request);

        try {
            if (accessToken != null && jwtUtil.validateToken(accessToken)) {

                String id = jwtUtil.extractToken(accessToken);
                User user = repository.findById(id).orElseThrow(() -> AuthException.of(FAIL_AUTHENTICATION));

                UserAuthentication authentication = new UserAuthentication(user);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (TokenExpiredException e) {
            handleException(response, AuthException.of(EXPIRED_TOKEN));
            return;
        } catch (ApiException e) {
            handleException(response, e);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {

        String authorization = request.getHeader("Authorization");

        return (authorization != null && authorization.startsWith("Bearer "))
                ? authorization.substring(7)
                : null;
    }

    private void handleException(HttpServletResponse response, ApiException e) throws IOException {

        ApiResponse<?> apiResponse = ApiResponse.error(e);

        String content = new ObjectMapper().writeValueAsString(apiResponse);

        response.addHeader("Content-Type", "application/json");
        response.getWriter().write(content);
        response.getWriter().flush();
    }
}
