package uz.sb.storyservice.service.story;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import uz.sb.storyservice.domain.dto.request.StoryRequest;
import uz.sb.storyservice.domain.dto.response.StoryResponse;
import uz.sb.storyservice.domain.entity.StoryEntity;

import java.io.IOException;
import java.util.List;

@Service
public interface StoryService {

    StoryResponse save(StoryRequest storyRequest, MultipartFile file) throws IOException;

    void delete(Long id);

    List<StoryResponse> findAll();

    StoryEntity findById(Long id);

    StoryResponse update(Long id, StoryRequest storyRequest);

   ResponseEntity<byte[]> downloadFilesAsZip(Long userId);


}
