package pl.ladziak.workload.controllers;

import org.springframework.web.bind.annotation.*;
import pl.ladziak.workload.dto.UserDto;
import pl.ladziak.workload.request.UpdateUsersEmailRequest;
import pl.ladziak.workload.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @PutMapping("/{id}")
    public void updateUsersEmail(@PathVariable Long id, @RequestBody UpdateUsersEmailRequest request) {
        userService.updateUserEmail(id, request);
    }
}
