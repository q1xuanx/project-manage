package projects.manage.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String projectName;
    private String projectDescription;
    @ManyToOne
    private Users userCreate;
    @OneToMany
    private List<Users> members;
}
