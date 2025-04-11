package com.github.kmu_wink.seoul_in_culture.domain.event.$data_collection.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class EventRequest {
    @JsonProperty("TITLE")
    private String title;

    @JsonProperty("PROGRAM")
    private String description;
    @JsonProperty("CODENAME")
    private String category;

    @JsonProperty("GUNAME")
    private String district;
    @JsonProperty("PLACE")
    private String place;
    @JsonProperty("STRTDATE")
    private String startDate;
    @JsonProperty("END_DATE")
    private String endDate;

    @JsonProperty("ORG_NAME")
    private String orgName;
    @JsonProperty("USE_TRGT")
    private String useTarget;
    @JsonProperty("IS_FREE")
    private String isFree;
    @JsonProperty("USE_FEE")
    private String feeInfo;

    @JsonProperty("MAIN_IMG")
    private String mainImage;
    @JsonProperty("HMPG_ADDR")
    private String homepageUrl;
    @JsonProperty("RGSTDATE")
    private String registerDate;
    @JsonProperty("TICKET") //정보를 등록한 주체가 시민인지 기관인지
    private String ticketType;

    @JsonProperty("PLAYER") // 연주자, 공연자 등을 의미
    private String playerInfo;

}