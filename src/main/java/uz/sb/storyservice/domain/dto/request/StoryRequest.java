package uz.sb.storyservice.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class StoryRequest {
    private Long userId;

    private String comment;

    private String mediaType;

    private String contentUrl;
}
