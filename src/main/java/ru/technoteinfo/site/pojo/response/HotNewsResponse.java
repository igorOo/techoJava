package ru.technoteinfo.site.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.technoteinfo.site.entities.Posts;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotNewsResponse {

    private Long id;

    private Posts postId;

    @JsonProperty("created_at")
    private Instant createdAt;

}
