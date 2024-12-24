package projects.manage.services;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projects.manage.entities.Users;
import projects.manage.repositories.UserRepository;
import projects.manage.request.RegisterRequest;
import projects.manage.response.ApiResponse;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    public ApiResponse login (String username, String password) {
        if (userRepository.existsByUsername(username)) {
            Users user = userRepository.findByUsername(username);
            boolean isExact = passwordEncoder.matches(password, user.getPasswords());
            if (isExact) {
                String token = jwtService.generateToken(user.getUsername());
                return new ApiResponse(200, token, true, user);
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
        user.setUsername(registerRequest.getUserName());
        String passcode = passwordEncoder.encode(registerRequest.getPassword());
        user.setPasswords(passcode);
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNumber(registerRequest.getPhone());
        user.setFullName(registerRequest.getFullName());
        userRepository.save(user);
        return new ApiResponse(200, "User created", true, user);
    }
    public Users getUserById(int id){
        return userRepository.findById(id).orElse(null);
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

    public String randomCodeGen(){
        StringBuilder codeGen = new StringBuilder();
        int turn = 0;
        Random rand  = new Random();
        while(turn < 6){
            codeGen.append(rand.nextInt(10));
            turn++;
        }
        return codeGen.toString();
    }

    public ApiResponse restPasswordRequest(String email) {
        Users user = userRepository.findByEmail(email);
        if (user == null) {
            return new ApiResponse(404, "User not found", false, null);
        }
        if (user.getTokenDate() != null) {
            Instant instant = Instant.now();
            Duration duration = Duration.between(user.getTokenDate(), instant);
            if (duration.toMinutes() > 5){
                ZoneId vnTime = ZoneId.of("UTC+7");
                LocalDateTime now = LocalDateTime.now(vnTime);
                user.setTokenRest(randomCodeGen());
                user.setTokenDate(now);
                userRepository.save(user);
                emailService.sendRestPassword(user);
                return new ApiResponse(200, "Send Confirm Code Success", true, user);
            }
            return new ApiResponse(200, "Please check email again we have send it", true, null);
        }
        ZoneId vnTime = ZoneId.of("UTC+7");
        LocalDateTime now = LocalDateTime.now(vnTime);
        user.setTokenDate(now);
        user.setTokenRest(randomCodeGen());
        userRepository.save(user);
        emailService.sendRestPassword(user);
        return new ApiResponse(200, "Send Confirm Password To Your email Success", true, null);
    }

    public ApiResponse validCodeCheck(String code) {
        Users user = userRepository.findByTokenRest(code);
        if (user == null) {
            return new ApiResponse(404, "User not found", false, null);
        }
        return new ApiResponse(200, "User found", true, user.getIdUser());
    }

    public ApiResponse resetPassword(int idUser, String password) {
        Optional<Users> findUser = userRepository.findById(idUser);
        if(findUser.isPresent()){
            Users user = findUser.get();
            user.setPasswords(passwordEncoder.encode(password));
            userRepository.save(user);
            return new ApiResponse(200, "User updated", true, user);
        }
        return new ApiResponse(404, "User not found", false, null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username);
        if(user != null){
            return new UserInfoDetails(user);
        }
        return null;
    }
}
