package uz.sb.storyservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.sb.storyservice.domain.entity.StoryEntity;
import uz.sb.storyservice.domain.entity.StoryViews;

import java.util.Optional;

public interface StoryViewsRepository extends JpaRepository<StoryViews, Long> {

    Optional<StoryViews> findByUserIdAndStoryId(Long userId, Long storyId);
}
