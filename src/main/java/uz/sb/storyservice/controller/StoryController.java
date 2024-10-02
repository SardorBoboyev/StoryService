package uz.sb.storyservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.sb.storyservice.domain.dto.request.StoryRequest;
import uz.sb.storyservice.domain.dto.response.StoryResponse;
import uz.sb.storyservice.service.story.StoryServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/story")
public class StoryController {

    private final StoryServiceImpl storyService;

    @PostMapping("/create")
    private StoryResponse create(@RequestBody StoryRequest storyRequest) {
        return storyService.save(storyRequest);
    }

    @GetMapping("/get-all")
    private List<StoryResponse> getAll() {
        return storyService.findAll();
    }

    @PutMapping("/update/{id}")
    private StoryResponse update(@PathVariable("id") Long id, @RequestBody StoryRequest storyRequest) {
        return storyService.update(id, storyRequest);
    }

    @DeleteMapping("/{id}")
    private void delete(@PathVariable("id") Long id) {
        storyService.delete(id);
    }
}
