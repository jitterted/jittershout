
package com.jitterted;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@SuppressWarnings("unused")
public class TwitchTeam {
    @JsonProperty("_id")
    private long id;
    private Object background;
    private String banner;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("display_name")
    private String displayName;
    private String info;
    private String logo;
    private String name;
    @JsonProperty("updated_at")
    private String updatedAt;
    private List<TwitchUser> users;

}
