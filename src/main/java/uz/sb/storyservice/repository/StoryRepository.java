package uz.sb.storyservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.sb.storyservice.domain.entity.StoryEntity;

import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<StoryEntity, Long> {
List<StoryEntity> findAllByUserId(Long userId);
}
