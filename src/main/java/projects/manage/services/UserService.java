package projects.manage.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projects.manage.entities.User;
import projects.manage.repositories.UserRepository;
import projects.manage.request.RegisterRequest;
import projects.manage.response.ApiResponse;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public ApiResponse login(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            User user = userRepository.findByUsernameAndPassword(username, password);
            if (user != null) {
                return new ApiResponse(200, "Login success", true, user);
            }
            return new ApiResponse(404, "Password or Username incorrect", false, null);
        }else {
            return new ApiResponse(404, "User not found", false, null);
        }
    }
    public ApiResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new ApiResponse(400, "Username already exists", false, null);
        }else if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ApiResponse(404, "Email address already in use", false, null);
        }else if (userRepository.existsByPhoneNumber(registerRequest.getPhone())) {
            return new ApiResponse(404, "Phone number already in use", false, null);
        }
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNumber(registerRequest.getPhone());
        userRepository.save(user);
        return new ApiResponse(200, "User created", true, user);
    }
    public ApiResponse getUser(int userId) {
        Optional<User> findUser = userRepository.findById(userId);
        if (findUser.isPresent()) {
            User user = findUser.get();
            return new ApiResponse(200, "User found", true, user);
        }
        return new ApiResponse(404, "User not found", false, null);
    }
}
