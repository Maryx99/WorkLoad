package pl.ladziak.workload.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ladziak.workload.dto.UserDto;
import pl.ladziak.workload.dto.WorkHourDto;
import pl.ladziak.workload.models.User;
import pl.ladziak.workload.models.WorkHour;
import pl.ladziak.workload.repositories.UserRepository;
import pl.ladziak.workload.repositories.WorkHourRepository;
import pl.ladziak.workload.request.CreateHoursRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkHourService {
    private final UserRepository userRepository;
    private final WorkHourRepository workHourRepository;

    public List<WorkHourDto> getWorkHoursCurrentUser(Long id) {
        List<WorkHour> workHoursByUserEmail = workHourRepository.getWorkHoursByUserEmail(id);
        return workHoursByUserEmail.stream()
                .map(workHour -> WorkHourDto.builder()
                        .id(workHour.getId())
                        .start(workHour.getStart())
                        .end(workHour.getEnd())
                        .build())
                //.collect(Collectors.toList())
                .toList();
    }

    public void createHours(CreateHoursRequest request) {
        User user = userRepository.getReferenceById(1L);
        WorkHour workHour = WorkHour.builder() //zastosowalem tutaj builder pattern o ktorym mowilem wczesniej na zajeciach
                .start(request.start())
                .end(request.end())
                .user(user)
                .build();
        workHourRepository.save(workHour);

    }
}
