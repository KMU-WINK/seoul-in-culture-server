package com.github.kmu_wink.seoul_in_culture.common.logging;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {

        long start = System.currentTimeMillis();

        filterChain.doFilter(request, response);

        long end = System.currentTimeMillis();
        long elapsed = end - start;

        log.info("{} {} {} {} {}ms",
                request.getMethod(),
                request.getRequestURI(),
                getIp(request),
                response.getStatus(),
                elapsed
        );
    }

    private String getIp(HttpServletRequest request) {

        String xfHeader = request.getHeader("X-Forwarded-For");

        return xfHeader == null ? request.getRemoteAddr() : xfHeader.split(",")[0];
    }
}