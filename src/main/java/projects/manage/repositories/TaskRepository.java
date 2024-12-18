package projects.manage.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projects.manage.entities.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

}
