package projects.manage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProjectDto {
    private int id;
    private String name;
    private String description;
    private UsersDto created;
    private List<UsersDto> listMem;
}
