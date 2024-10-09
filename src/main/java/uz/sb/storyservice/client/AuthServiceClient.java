package uz.sb.storyservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uz.sb.domain.dto.response.UserResponse;
import uz.sb.storyservice.config.FeignConfig;


@FeignClient(name = "AUTH-SERVICE", configuration = FeignConfig.class)
public interface AuthServiceClient {

    @GetMapping("api/auth/find-by-id/{id}")
    UserResponse findById(@PathVariable Long id);
}
