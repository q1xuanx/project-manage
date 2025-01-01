package projects.manage.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projects.manage.entities.Project;
import projects.manage.entities.Task;
import projects.manage.entities.Users;
import projects.manage.repositories.ProjectRepository;
import projects.manage.repositories.TaskRepository;
import projects.manage.repositories.UserRepository;
import projects.manage.request.CreateTaskRequest;
import projects.manage.response.ApiResponse;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    public ApiResponse addTask(CreateTaskRequest createTaskRequest) {
        Optional<Project> findProject = projectRepository.findById(createTaskRequest.getIdProject());
        if (findProject.isEmpty()) {
            return new ApiResponse(404, "not found project", false, null);
        }
        Project project = findProject.get();
        List<Users> userDoQuest = new ArrayList<>();
        Task task = new Task();
        for (int i : createTaskRequest.getListAssign()){
            Optional<Users> findUser = userRepository.findById(i);
            if (findUser.isEmpty()) {
                return new ApiResponse(404, "not found user", false, null);
            }
            userDoQuest.add(findUser.get());
        }
        Optional<Users> getUserCreated = userRepository.findById(createTaskRequest.getUserCreated());
        if (getUserCreated.isEmpty()) {
            return new ApiResponse(404, "not found user", false, null);
        }
        task.setTitle(createTaskRequest.getTitle());
        task.setDescription(createTaskRequest.getDescription());
        task.setAssignees(userDoQuest);
        task.setDeadline(createTaskRequest.getDeadline());
        task.setCreated(getUserCreated.get());
        task.setIdProject(project);
        task.setStatus(createTaskRequest.getStatus());
        task.setPriority(createTaskRequest.getPriority());
        taskRepository.save(task);
        return new ApiResponse(200, "task created", true, null);
    }
    public ApiResponse getListTaskOrderByPriority(int idProject){
        Optional<Project> findProject = projectRepository.findById(idProject);
        if (findProject.isEmpty()) {
            return new ApiResponse(404, "not found project", false, null);
        }
        List<Task> tasks = taskRepository.findTaskByIdProjectOrderByPriorityDesc(findProject.get());
        return new ApiResponse(200, "tasks", true, tasks);
    }
    public ApiResponse getTaskById (int idTask){
        Optional<Task> findTask = taskRepository.findById(idTask);
        if (findTask.isEmpty()) {
            return new ApiResponse(404, "not found task", false, null);
        }
        return new ApiResponse(200, "task", true, findTask.get());
    }
    public ApiResponse editTask(int idTask, Task task) {
        Optional<Task> findTask = taskRepository.findById(idTask);
        if (findTask.isEmpty()) {
            return new ApiResponse(404, "not found task", false, null);
        }
        Task taskToEdit = findTask.get();
        taskToEdit.setTitle(task.getTitle());
        taskToEdit.setDescription(task.getDescription());
        taskToEdit.setAssignees(task.getAssignees());
        taskToEdit.setDeadline(task.getDeadline());
        taskToEdit.setPriority(task.getPriority());
        taskRepository.save(taskToEdit);
        return new ApiResponse(200, "task updated", true, task);
    }
    public ApiResponse assignMemberToTask(int idTask, int idUser) {
        Optional<Task> findTask = taskRepository.findById(idTask);
        if (findTask.isEmpty()) {
            return new ApiResponse(404, "not found task", false, null);
        }
        Optional<Users> findUser = userRepository.findById(idUser);
        if (findUser.isEmpty()) {
            return new ApiResponse(404, "not found user", false, null);
        }
        Task task = findTask.get();
        task.getAssignees().add(findUser.get());
        taskRepository.save(task);
        return new ApiResponse(200, "task assigned to " + findUser.get().getEmail(), true, null);
    }
}
