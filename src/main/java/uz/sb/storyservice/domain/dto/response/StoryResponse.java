package uz.sb.storyservice.domain.dto.response;

import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class StoryResponse {
    private Long id;

    private Long userId;

    private String mediaType;

    private String comment;

    private String contentUrl;

    private LocalDateTime createdAt;


}
