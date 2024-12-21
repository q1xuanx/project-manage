package projects.manage.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projects.manage.request.LoginRequest;
import projects.manage.request.RegisterRequest;
import projects.manage.request.RestPasswordRequest;
import projects.manage.response.ApiResponse;
import projects.manage.services.UserService;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }
    @PostMapping("/register")
    public ApiResponse register(@RequestBody RegisterRequest registerRequest) {
        return userService.register(registerRequest);
    }
    @GetMapping("/users/{idUser}")
    public ApiResponse getUser(@PathVariable int idUser){
        return userService.getUser(idUser);
    }
    @GetMapping("/get-all")
    @PreAuthorize("hasAuthority('admin')")
    public ApiResponse getAllUsers(){
        return userService.getAllUsers();
    }
    @PostMapping("/rest-pass")
    public ApiResponse requestRestPass(@RequestBody String email){
        return userService.restPasswordRequest(email);
    }
    @PostMapping("/check-code")
    public ApiResponse checkCode(@RequestBody String code){
        return userService.validCodeCheck(code);
    }
    @PostMapping("/rest")
    public ApiResponse restPassword(@RequestBody RestPasswordRequest restPasswordRequest){
        return userService.resetPassword(restPasswordRequest.getUserId(), restPasswordRequest.getPassword());
    }
}
