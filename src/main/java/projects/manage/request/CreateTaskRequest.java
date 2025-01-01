package projects.manage.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class CreateTaskRequest {
    private String title;
    private String description;
    private int status;
    private int priority;
    private List<Integer> listAssign = new ArrayList<Integer>();
    private LocalDateTime deadline;
    private int userCreated;
    private int idProject;
}
