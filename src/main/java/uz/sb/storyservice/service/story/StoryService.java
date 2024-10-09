package uz.sb.storyservice.service.story;

import org.springframework.stereotype.Service;
import uz.sb.domain.dto.request.StoryRequest;
import uz.sb.domain.dto.response.StoryResponse;



import uz.sb.storyservice.domain.entity.StoryEntity;

import java.util.List;

@Service
public interface StoryService {

    StoryResponse save(StoryRequest storyRequest);

    void delete(Long id);

    List<StoryResponse> findAll();

    StoryEntity findById(Long id);

    StoryResponse update(Long id, StoryRequest storyRequest);




}
