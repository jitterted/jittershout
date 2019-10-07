
package com.jitterted;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@SuppressWarnings("unused")
public class TwitchUser {

    @JsonProperty("_id")
    private long id;
    @JsonProperty("broadcaster_language")
    private String broadcasterLanguage;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("display_name")
    private String displayName;
    private long followers;
    private String game;
    private String language;
    private String logo;
    private Boolean mature;
    private String name;
    private Boolean partner;
    @JsonProperty("profile_banner")
    private String profileBanner;
    @JsonProperty("profile_banner_background_color")
    private Object profileBannerBackgroundColor;
    private String status;
    @JsonProperty("updated_at")
    private String updatedAt;
    private String url;
    @JsonProperty("video_banner")
    private Object videoBanner;
    private long views;

}
