package uz.sb.storyservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

@Entity(name = "stories")
public class StoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String mediaType;

    private String contentUrl;

    private String comment;

    private Long storyCount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}
