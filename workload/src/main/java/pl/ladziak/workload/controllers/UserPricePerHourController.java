package pl.ladziak.workload.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.ladziak.workload.request.CreateNewUserPricePerHour;
import pl.ladziak.workload.services.UserPricePerHourService;

@RestController
@RequestMapping("/user-price")
@RequiredArgsConstructor
public class UserPricePerHourController {

    private final UserPricePerHourService userPricePerHourService;
    @PostMapping("/{userUuid}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void createNewUserPricePerHour(@PathVariable String userUuid,@Valid @RequestBody CreateNewUserPricePerHour request) {
        userPricePerHourService.createNewUserPricePerHour(userUuid, request);

    }
}
