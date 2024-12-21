package projects.manage.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class RestPasswordRequest {
    private int userId;
    private String password;
}
