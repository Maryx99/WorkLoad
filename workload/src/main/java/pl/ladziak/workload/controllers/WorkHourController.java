package pl.ladziak.workload.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.ladziak.workload.dto.UserDto;
import pl.ladziak.workload.dto.WorkHourDto;
import pl.ladziak.workload.request.CreateHoursRequest;
import pl.ladziak.workload.services.WorkHourService;

import java.util.List;

@RestController
@RequestMapping("/workhours")
@RequiredArgsConstructor
public class WorkHourController {
    private final WorkHourService workHourService;

    @GetMapping("/{id}")
    //@PreAuthorize()
    public List<WorkHourDto> getWorkHoursCurrentUser(@PathVariable Long id) {
        return workHourService.getWorkHoursCurrentUser(id);
    }

    @PostMapping
    public void createHours(@RequestBody CreateHoursRequest request) {
        workHourService.createHours(request);
    }

}
