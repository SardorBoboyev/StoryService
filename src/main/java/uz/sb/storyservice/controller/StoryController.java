package uz.sb.storyservice.controller;

import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.sb.storyservice.domain.dto.request.StoryRequest;
import uz.sb.storyservice.domain.dto.response.StoryResponse;
import uz.sb.storyservice.service.story.StoryServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@MultipartConfig(maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 50 * 1024 * 1024,
        fileSizeThreshold = 10 * 1024)
@RequestMapping("/api/story")
public class StoryController {

    private final StoryServiceImpl storyService;

    @PostMapping("/create")
    public ResponseEntity<StoryResponse> create(
            @RequestPart("file") MultipartFile file,
            @RequestPart("jsonData") StoryRequest storyRequest) {
        StoryResponse save = storyService.save(storyRequest, file);
        return new ResponseEntity<>(save, HttpStatus.CREATED);

    }


    /**
     * @param userId Идентификатор пользователя.
     * @return ResponseEntity<byte[]> Объект, содержащий ZIP-файл.
     *
     * <p>Чтобы просмотреть содержимое ZIP-файла, необходимо перейти в springConfig
     * и изменить настройки whiteList.</p>
     */
    @GetMapping("/files/{userId}")
    public ResponseEntity<byte[]> getFiles(@PathVariable Long userId) {
        return storyService.downloadFilesAsZip(userId);
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
