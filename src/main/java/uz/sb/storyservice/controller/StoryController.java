package uz.sb.storyservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.sb.domain.dto.request.StoryRequest;
import uz.sb.domain.dto.response.StoryResponse;
import uz.sb.storyservice.service.story.StoryServiceImpl;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/story")
public class StoryController {

    private final StoryServiceImpl storyService;

    @PostMapping("/create")
    private StoryResponse create(@RequestBody StoryRequest storyRequest, @RequestParam("file") MultipartFile file) throws IOException {
        return storyService.save(storyRequest, file);
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
