package uz.sb.storyservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uz.sb.storyservice.config.FeignConfig;
import uz.sb.storyservice.domain.dto.response.UserResponse;


@FeignClient(name = "AUTH-SERVICE", configuration = FeignConfig.class)
public interface AuthServiceClient {

    @GetMapping("api/auth/find-by-id/{id}")
    UserResponse findById(@PathVariable Long id);
}
