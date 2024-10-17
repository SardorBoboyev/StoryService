package uz.sb.storyservice.domain.dto.response;

import lombok.*;
import uz.sb.domain.dto.response.BaseResponse;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse extends BaseResponse {
    private String firstName;
    private String lastName;
    private String username;
    private String phoneNumber;
}
