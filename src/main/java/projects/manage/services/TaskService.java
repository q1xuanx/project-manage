package projects.manage.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projects.manage.repositories.TaskRepository;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;


}
