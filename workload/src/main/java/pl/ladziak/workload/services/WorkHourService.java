package pl.ladziak.workload.services;

import org.springframework.stereotype.Service;
import pl.ladziak.workload.dto.UserDto;
import pl.ladziak.workload.models.User;
import pl.ladziak.workload.models.WorkHour;
import pl.ladziak.workload.repositories.UserRepository;
import pl.ladziak.workload.repositories.WorkHourRepository;
import pl.ladziak.workload.request.CreateHoursRequest;

import java.util.List;

@Service
public class WorkHourService {
    private final UserRepository userRepository;
    private final WorkHourRepository workHourRepository;

    public WorkHourService(UserRepository userRepository, WorkHourRepository workHourRepository) {
        this.userRepository = userRepository;
        this.workHourRepository = workHourRepository;
    }

    public List<UserDto> getHours() {
        return userRepository.findAll().stream()
                .map(this::mapToUserDto)
                .toList();
    }
    public UserDto mapToUserDto(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole());
    }

    public void createHours(CreateHoursRequest request) {
        User user = userRepository.getReferenceById(1L);
        WorkHour workHour = new WorkHour(request.start(), request.end(), user);
        workHourRepository.save(workHour);

    }
}
