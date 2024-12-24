package projects.manage.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projects.manage.entities.Project;
import projects.manage.entities.Users;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
}
