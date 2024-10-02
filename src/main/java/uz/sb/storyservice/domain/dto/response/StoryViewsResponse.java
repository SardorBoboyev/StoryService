package uz.sb.storyservice.domain.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class StoryViewsResponse {
    private Long id;
    private Long userId;
    private Long storyId;
    private Long viewCount;
    private LocalDateTime viewedAt;
}
