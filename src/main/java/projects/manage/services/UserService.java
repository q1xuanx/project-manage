package projects.manage.services;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projects.manage.entities.Users;
import projects.manage.repositories.UserRepository;
import projects.manage.request.RegisterRequest;
import projects.manage.response.ApiResponse;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public ApiResponse login (String username, String password) {
        if (userRepository.existsByUsername(username)) {
            Users user = userRepository.findByUsernameAndPasswords(username, password);
            if (user != null) {
                return new ApiResponse(200, "Login success", true, user);
            }
            return new ApiResponse(404, "Password or Username incorrect", false, null);
        }else {
            return new ApiResponse(404, "User not found", false, null);
        }
    }
    public ApiResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUserName())) {
            return new ApiResponse(400, "Username already exists", false, null);
        }else if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ApiResponse(404, "Email address already in use", false, null);
        }else if (userRepository.existsByPhoneNumber(registerRequest.getPhone())) {
            return new ApiResponse(404, "Phone number already in use", false, null);
        }
        Users user = new Users();
        user.setUsername(registerRequest.getFullName());
        user.setPasswords(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNumber(registerRequest.getPhone());
        userRepository.save(user);
        return new ApiResponse(200, "User created", true, user);
    }
    public ApiResponse getUser(int userId) {
        Optional<Users> findUser = userRepository.findById(userId);
        if (findUser.isPresent()) {
            Users user = findUser.get();
            return new ApiResponse(200, "User found", true, user);
        }
        return new ApiResponse(404, "User not found", false, null);
    }
    public ApiResponse getAllUsers(){
        List<Users> findAll = userRepository.findAll();
        if (findAll.isEmpty()){
            return new ApiResponse(404, "List user is empty", false, null);
        }
        return new ApiResponse(200, "Users found", true, findAll);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
