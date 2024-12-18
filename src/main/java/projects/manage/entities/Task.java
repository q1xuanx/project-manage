package projects.manage.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idTask;
    private String title;
    private String description;
    private int priority;
    private LocalDateTime deadline;
    private int status;
    @ManyToOne
    private Users created;
    @OneToMany
    private List<Users> assignees;
    @ManyToOne
    private Project idProject;
}
