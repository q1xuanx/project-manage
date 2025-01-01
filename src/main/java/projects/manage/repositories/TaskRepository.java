package projects.manage.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projects.manage.entities.Project;
import projects.manage.entities.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findTaskByIdProjectOrderByPriorityDesc(Project idProject);
}
