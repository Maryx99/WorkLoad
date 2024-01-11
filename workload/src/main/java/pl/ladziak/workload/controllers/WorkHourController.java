package pl.ladziak.workload.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.ladziak.workload.dto.WorkHourDto;
import pl.ladziak.workload.models.User;
import pl.ladziak.workload.request.CreateHoursRequest;
import pl.ladziak.workload.services.WorkHourService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/workhours")
@RequiredArgsConstructor
public class WorkHourController {
    private final WorkHourService workHourService;

    @GetMapping
    public List<WorkHourDto> getWorkHoursCurrentUserByUserId(@AuthenticationPrincipal User loggedUser) {
        return workHourService.getWorkHoursCurrentUserByUserId(loggedUser.getId());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<WorkHourDto> getWorkHoursForAllUsers(@RequestParam LocalDate from, @RequestParam LocalDate to) {
        return workHourService.getWorkHoursForAllUsers(from, to);
    }
    @PostMapping
    public void createHours(@AuthenticationPrincipal User loggedUser, @RequestBody CreateHoursRequest request) {
        workHourService.createHours(loggedUser, request);
    }

    @GetMapping("/xx/{uuid}")
    @PreAuthorize("#uuid == #principal.uuid") // w ten sposob mozna walidowac czy dany user powinien miec dostep do zasobu
//    @PreAuthorize("#uuid == #principal.uuid or hasRole('ADMIN')") // mozna to tez robic po roli
    public void aa(@AuthenticationPrincipal User principal, @PathVariable String uuid) {
        System.out.println(principal);
    }
}
