package uz.sb.storyservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${aws.s3.access-key-id}")
    private String accessKey;

    @Value("${aws.s3.secret-access-key}")
    private String secretKey;

    @Bean
    public AwsCredentials awsCreadentials(){
        return AwsBasicCredentials.create(
                accessKey,
                secretKey
        );
    }

    @Bean
    public S3Client s3Client(){
        return S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(this::awsCreadentials)
                .build();
    }
}
