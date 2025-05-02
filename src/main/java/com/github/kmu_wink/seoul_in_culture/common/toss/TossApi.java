package com.github.kmu_wink.seoul_in_culture.common.toss;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiException;
import com.github.kmu_wink.seoul_in_culture.common.property.TossProperty;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestInstance;
import kong.unirest.core.json.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TossApi {

    private final TossProperty tossProperty;

    private String authHeader;

    @PostConstruct
    public void initKey() {

        this.authHeader = "Basic " + new String(
                Base64.getEncoder().encode((tossProperty.getKey() + ":").getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8
        );
    }

    public String confirmPayment(String orderId, String paymentKey, int amount) {

        try (UnirestInstance instance = Unirest.spawnInstance()) {

            JSONObject response = instance.post("https://api.tosspayments.com/v1/payments/confirm")
                    .header("Content-Type", "application/json")
                    .header("Authorization", authHeader)
                    .body(Map.ofEntries(
                            Map.entry("orderId", orderId),
                            Map.entry("paymentKey", paymentKey),
                            Map.entry("amount", amount)
                    ))
                    .asJson()
                    .getBody()
                    .getObject();

            if (response.has("code")) {
                throw new ApiException(response.getString("message"));
            }

            return response.getString("paymentKey");
        }
    }

    public void refundPayment(String paymentKey) {

        try (UnirestInstance instance = Unirest.spawnInstance()) {

            JSONObject response = instance.post("https://api.tosspayments.com/v1/payments/%s/cancel".formatted(
                            paymentKey))
                    .header("Content-Type", "application/json")
                    .header("Authorization", authHeader)
                    .body(Map.of("cancelReason", "보증금 반환"))
                    .asJson()
                    .getBody()
                    .getObject();

            if (response.has("code")) {
                throw new ApiException(response.getString("message"));
            }
        }
    }
}
