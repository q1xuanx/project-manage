package projects.manage.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projects.manage.dto.ProjectDto;
import projects.manage.dto.UsersDto;
import projects.manage.entities.Project;
import projects.manage.entities.Users;
import projects.manage.repositories.ProjectRepository;
import projects.manage.repositories.UserRepository;
import projects.manage.request.CreateProjectRequest;
import projects.manage.response.ApiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    public ApiResponse createNewProject(CreateProjectRequest createProjectRequest) {
        Optional<Users> created = userRepository.findById(createProjectRequest.getIdUserCreated());
        if (created.isEmpty()){
            return new ApiResponse(404, "not found user create", false, null);
        }
        List<Users> listMember = new ArrayList<>();
        for (int userId : createProjectRequest.getListMembers()){
            Optional<Users> user = userRepository.findById(userId);
            if(user.isEmpty()){
                return new ApiResponse(404, "not found member with that id", false, null);
            }
            listMember.add(user.get());
        }
        Project project = new Project();
        project.setProjectName(createProjectRequest.getProjectName());
        project.setProjectDescription(createProjectRequest.getDescription());
        project.setUserCreate(created.get());
        project.setMembers(listMember);
        projectRepository.save(project);
        ProjectDto request = makeResponse(project, created.get(), listMember);
        return new ApiResponse(200, "create success", true, request);
    }
    public ApiResponse getProject(int id){
        return new ApiResponse(200, "get success", true, projectRepository.findById(id));
    }
    public ApiResponse getManageProject(int idUser){
        Optional<Users> users = userRepository.findById(idUser);
        if(users.isEmpty()){
            return new ApiResponse(404, "not found user", false, null);
        }
        List<Project> projects = projectRepository.findAll().stream().filter(s -> s.getMembers().contains(users.get())).toList();
        List<ProjectDto> projectDtoList = new ArrayList<>();
        for (Project project : projects) {
            ProjectDto request = makeResponse(project, users.get(), project.getMembers());
            projectDtoList.add(request);
        }
        return new ApiResponse(200, "get success", true, projectDtoList);
    }
    public Project getProjectById(int id) {
        return projectRepository.findById(id).orElse(null);
    }
    public ApiResponse assignMemberToProject(int idProject, String username){
        ApiResponse findUser = findUser(username);
        if (findUser.getCode() != 200){
            return new ApiResponse(404, "not found user", false, null);
        }
        Users user = (Users) findUser.getData();
        Optional<Project> findProject = projectRepository.findById(idProject);
        if (findProject.isEmpty()){
            return new ApiResponse(404, "not found project", false, null);
        }
        Project project = findProject.get();
        boolean isExist = project.getMembers().stream().anyMatch(usr -> usr.getFullName().equals(user.getUsername()));
        if (isExist){
            return new ApiResponse(404, "user already exist", false, null);
        }
        project.getMembers().add(user);
        projectRepository.save(project);
        return new ApiResponse(200, "assign success", true, "user has add to project");
    }
    public ApiResponse findUser(String userName){
        Users findUser = userRepository.findByUsername(userName);
        if (findUser == null){
            return new ApiResponse(404, "not found user", false, null);
        }
        return new ApiResponse(200, "get success", true, findUser);
    }
    public ApiResponse removeMemberFromProject(int idProject, String username){
        ApiResponse findUser = findUser(username);
        if (findUser.getCode() != 200){
            return new ApiResponse(404, "not found user", false, null);
        }
        Users user = (Users) findUser.getData();
        Optional<Project> findProject = projectRepository.findById(idProject);
        if (findProject.isEmpty()){
            return new ApiResponse(404, "not found project", false, null);
        }
        Project project = findProject.get();
        boolean remove = project.getMembers().remove(user);
        if (remove){
            projectRepository.save(project);
            return new ApiResponse(200, "remove success", true, null);
        }
        return new ApiResponse(400, "error while remove member, please try again", false, null);
    }
    public ProjectDto makeResponse(Project project, Users users, List<Users> members) {
        UsersDto createdUser = new UsersDto(users.getIdUser(), users.getUsername());
        List<UsersDto> member = new ArrayList<>();
        for (Users usr : members){
            UsersDto user = new UsersDto(usr.getIdUser(), usr.getUsername());
            member.add(user);
        }
        return new ProjectDto(project.getId(), project.getProjectName(), project.getProjectDescription(), createdUser, member);
    }
}
