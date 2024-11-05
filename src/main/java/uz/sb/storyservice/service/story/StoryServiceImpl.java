package uz.sb.storyservice.service.story;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import uz.sb.storyservice.client.AuthServiceClient;
import uz.sb.storyservice.domain.dto.request.StoryRequest;
import uz.sb.storyservice.domain.dto.response.StoryResponse;
import uz.sb.storyservice.domain.dto.response.UserResponse;
import uz.sb.storyservice.domain.entity.StoryEntity;
import uz.sb.storyservice.domain.exception.DataNotFoundException;
import uz.sb.storyservice.repository.StoryRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;


    private final S3Client s3Client;
    private final StoryRepository storyRepository;
    private final AuthServiceClient authServiceClient;


    @Override
    public StoryResponse save(StoryRequest storyRequest, MultipartFile file) {
        UserResponse userRes = authServiceClient.findById(storyRequest.getUserId());

        if (Objects.isNull(userRes)) {
            throw new DataNotFoundException("User not found");
        }


        String contentUrl = uploadFile(file);

        StoryEntity story = StoryEntity.builder()
                .userId(storyRequest.getUserId())
                .comment(storyRequest.getComment())
                .mediaType(file.getContentType())
                .contentUrl(contentUrl)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(24))
                .build();

        storyRepository.save(story);

        return StoryResponse.builder()
                .id(story.getId())
                .comment(story.getComment())
                .userId(story.getUserId())
                .mediaType(file.getContentType())
                .contentUrl(story.getContentUrl())
                .createdAt(story.getCreatedAt())
                .build();
    }

    private String uploadFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("Failed to upload empty file");
        }

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                // .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        try {

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
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
                .createdAt(updatedStory.getCreatedAt())
                .comment(updatedStory.getComment())
                .build();
    }

    @Override
    public ResponseEntity<byte[]> downloadFilesAsZip(Long userId) {
        List<StoryEntity> stories = storyRepository.findAllByUserId(userId);

        if (stories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            for (StoryEntity story : stories) {
                String contentUrl = story.getContentUrl();
                byte[] fileBytes = downloadFile(contentUrl);

                ZipEntry zipEntry = new ZipEntry(extractFileNameFromUrl(contentUrl));
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.write(fileBytes);
                zipOutputStream.closeEntry();

            }

            zipOutputStream.finish();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"files.zip\"");
            headers.setContentLength(byteArrayOutputStream.size());

            return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<StoryResponse> findAllStoriesByUserId(Long userId) {
        return storyRepository.findAllByUserId(userId).stream()
                .map(storyEntity -> StoryResponse.builder()
                        .id(storyEntity.getId())
                        .userId(storyEntity.getUserId())
                        .mediaType(storyEntity.getMediaType())
                        .comment(storyEntity.getComment())
                        .contentUrl(storyEntity.getContentUrl())
                        .createdAt(storyEntity.getCreatedAt())
                        .build()
                )
                .collect(Collectors.toList());
    }



    private byte[] downloadFile(String contentUrl) {
        String fileName = extractFileNameFromUrl(contentUrl);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest)) {
            return s3Object.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file from S3", e);
        }
    }

    private String extractFileNameFromUrl(String contentUrl) {
        return contentUrl.substring(contentUrl.lastIndexOf("/") + 1);
    }

}
