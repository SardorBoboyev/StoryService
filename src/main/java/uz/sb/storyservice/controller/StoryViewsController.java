package uz.sb.storyservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.sb.storyservice.domain.dto.request.StoryViewsRequest;
import uz.sb.storyservice.domain.dto.response.StoryViewsResponse;
import uz.sb.storyservice.service.storyViews.StoryViewsServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/story/storyViews")
public class StoryViewsController {


    private final StoryViewsServiceImpl storyViewsService;

    @PostMapping("/create")
    public StoryViewsResponse createStoryView(@RequestBody StoryViewsRequest storyViews) {
        return storyViewsService.addStoryViewIfNotSeen(storyViews);
    }

}
