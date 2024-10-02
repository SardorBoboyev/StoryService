package uz.sb.storyservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

@Entity(name = "storyViews")
public class StoryViews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "story_id", nullable = false)
    private StoryEntity story;

    private Long viewCount;

    private LocalDateTime viewedAt;
}
