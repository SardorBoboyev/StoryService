package uz.sb.storyservice.service.storyViews;

import org.springframework.stereotype.Service;
import uz.sb.storyservice.domain.dto.request.StoryViewsRequest;
import uz.sb.storyservice.domain.dto.response.StoryViewsResponse;

@Service
public interface StoryViewsService {

    StoryViewsResponse addStoryViewIfNotSeen(StoryViewsRequest storyViewsRequest);
}
