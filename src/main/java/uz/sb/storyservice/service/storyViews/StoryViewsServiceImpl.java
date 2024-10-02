package uz.sb.storyservice.service.storyViews;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.sb.storyservice.domain.dto.request.StoryViewsRequest;
import uz.sb.storyservice.domain.dto.response.StoryViewsResponse;
import uz.sb.storyservice.domain.entity.StoryEntity;
import uz.sb.storyservice.domain.entity.StoryViews;
import uz.sb.storyservice.domain.exception.DataNotFoundException;
import uz.sb.storyservice.repository.StoryRepository;
import uz.sb.storyservice.repository.StoryViewsRepository;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoryViewsServiceImpl implements StoryViewsService {

    private final StoryRepository storyRepository;
    private final StoryViewsRepository storyViewsRepository;

    @Override
    public StoryViewsResponse addStoryViewIfNotSeen(StoryViewsRequest storyViewsRequest) {

        StoryEntity story = storyRepository.findById(storyViewsRequest.getStoryId())
                .orElseThrow(() -> new DataNotFoundException("Story not found"));

        Optional<StoryViews> existingViews = storyViewsRepository
                .findByUserIdAndStoryId(storyViewsRequest.getUserId(), storyViewsRequest.getStoryId());

        if (existingViews.isPresent()) {
            StoryViews view = existingViews.get();
            return new StoryViewsResponse(
                    view.getId(),
                    view.getUserId(),
                    view.getStory().getId(),
                    view.getViewCount(),
                    view.getViewedAt());
        }

        StoryViews newViews = new StoryViews();
        newViews.setUserId(storyViewsRequest.getUserId());
        newViews.setStory(story);
        newViews.setViewCount(1L);
        newViews.setViewedAt(LocalDateTime.now());

        StoryViews savedView = storyViewsRepository.save(newViews);

        story.setStoryCount(savedView.getViewCount() + 1);
        storyRepository.save(story);

        return new StoryViewsResponse(
                savedView.getId(),
                savedView.getUserId(),
                savedView.getStory().getId(),
                savedView.getViewCount(),
                savedView.getViewedAt());
    }
}
