package uz.sb.storyservice.service.story;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.sb.domain.dto.request.StoryRequest;
import uz.sb.domain.dto.response.StoryResponse;
import uz.sb.domain.dto.response.UserResponse;
import uz.sb.storyservice.client.AuthServiceClient;
import uz.sb.storyservice.domain.entity.StoryEntity;
import uz.sb.storyservice.domain.exception.DataNotFoundException;
import uz.sb.storyservice.repository.StoryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final AuthServiceClient authServiceClient;

    @Override
    public StoryResponse save(StoryRequest storyRequest) {

        UserResponse userRes = authServiceClient.findById(storyRequest.getUserId());

        if (Objects.isNull(userRes)) {
            throw new DataNotFoundException("User not found");
        }

                StoryEntity story = StoryEntity.builder()
                        .userId(storyRequest.getUserId())
                        .mediaType(storyRequest.getMediaType())
                        .contentUrl(storyRequest.getContentUrl())
                        .createdAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusHours(24))
                        .build();

        storyRepository.save(story);

        return StoryResponse.builder()
                .id(story.getId())
                .userId(story.getUserId())
                .mediaType(story.getMediaType())
                .contentUrl(story.getContentUrl())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public void delete(Long id) {
        storyRepository.deleteById(id);
    }

    @Override
    public List<StoryResponse> findAll() {
        return storyRepository.findAll().stream().map(stories -> StoryResponse.builder()
                .id(stories.getId())
                .userId(stories.getUserId())
                .mediaType(stories.getMediaType())
                .contentUrl(stories.getContentUrl())
                .createdAt(stories.getCreatedAt())
                .build()).collect(Collectors.toList());
    }

    @Override
    public StoryEntity findById(Long id) {
        return storyRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Story not found"));
    }

    @Override
    public StoryResponse update(Long id, StoryRequest storyRequest) {
        StoryEntity existsStory = findById(id);
        if (existsStory == null) {
            throw new DataNotFoundException("Story not found");
        }

        existsStory.setComment(storyRequest.getComment());
        StoryEntity updatedStory = storyRepository.save(existsStory);

        return StoryResponse.builder()
                .id(updatedStory.getId())
                .userId(updatedStory.getUserId())
                .mediaType(updatedStory.getMediaType())
                .contentUrl(updatedStory.getContentUrl())
                .createdAt(updatedStory.getCreatedAt())
                .comment(updatedStory.getComment())
                .build();
    }

}
