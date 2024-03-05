package pl.ladziak.workload.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.ladziak.workload.dto.UserDto;
import pl.ladziak.workload.models.User;
import pl.ladziak.workload.request.UpdateUsersEmailRequest;
import pl.ladziak.workload.services.UserService;

import java.time.LocalDate;
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

    @GetMapping("/{uuid}")
    @PreAuthorize("#uuid == #principal.uuid")
    public ResponseEntity<UserDto> getCurrentUser(@PathVariable String uuid, @AuthenticationPrincipal User principal) {
        return ResponseEntity.ok(mapToUserDto(principal));
    }

    private UserDto mapToUserDto(User user) {
        return new UserDto(user.getUuid(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole());
    }

    @PutMapping("/{id}")
    public void updateUsersEmail(@PathVariable Long id, @RequestBody UpdateUsersEmailRequest request) {
        userService.updateUserEmail(id, request);
    }

    @GetMapping("/{uuid}/salary")
    @PreAuthorize("#uuid == #principal.uuid")
    public void getUserSalary(@PathVariable String uuid, @AuthenticationPrincipal User principal,
                              @RequestParam LocalDate userHoursStart, @RequestParam LocalDate userHoursEnd) {


    }
}
