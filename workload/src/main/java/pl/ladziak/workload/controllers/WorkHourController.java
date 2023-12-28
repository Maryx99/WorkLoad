package pl.ladziak.workload.controllers;

import org.springframework.web.bind.annotation.*;
import pl.ladziak.workload.dto.UserDto;
import pl.ladziak.workload.request.CreateHoursRequest;
import pl.ladziak.workload.services.WorkHourService;

import java.util.List;

@RestController
@RequestMapping("/workhours")
public class WorkHourController {
    private final WorkHourService workHourService;

    public WorkHourController(WorkHourService workHourService) {
        this.workHourService = workHourService;
    }

    @GetMapping
    public List<UserDto> getHours() {
        return workHourService.getHours();
    }

    @PostMapping
    public void createHours(@RequestBody CreateHoursRequest request) {
        workHourService.createHours(request);
    }

}
