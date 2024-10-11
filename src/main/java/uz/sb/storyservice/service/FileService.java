package uz.sb.storyservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {


        @Value("${upload.dir}")
        private String uploadDir;

        public String uploadFile(MultipartFile file) throws IOException {

            String fileName = file.getOriginalFilename();

            if (fileName == null || fileName.isEmpty()) {
                throw new IllegalArgumentException("Failed to upload empty file");
            }

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path path = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), path);

            return path.toString();
        }
}
