package projects.manage.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse {
    private int code;
    private String message;
    private boolean status;
    private Object data;
}
