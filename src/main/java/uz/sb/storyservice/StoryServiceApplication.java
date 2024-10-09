package uz.sb.storyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class StoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoryServiceApplication.class, args);
    }

}
