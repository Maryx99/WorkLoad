package pl.ladziak.workload.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ladziak.workload.dto.WorkHourDto;
import pl.ladziak.workload.dto.WorkHourWithUserDto;
import pl.ladziak.workload.models.User;
import pl.ladziak.workload.models.WorkHour;
import pl.ladziak.workload.repositories.WorkHourRepository;
import pl.ladziak.workload.request.CreateHoursRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkHourService {
    private final WorkHourRepository workHourRepository;

    public List<WorkHourDto> getWorkHoursCurrentUserByUserId(Long id) {
        List<WorkHour> workHoursByUserId = workHourRepository.getWorkHoursByUserId(id);
        return workHoursByUserId.stream()
                .map(workHour -> WorkHourDto.builder()
                        .uuid(workHour.getUuid())
                        .start(workHour.getStart())
                        .end(workHour.getEnd())
                        .build())
                //.collect(Collectors.toList())
                .toList();
    }

    public void createHours(User loggedUser, CreateHoursRequest request) {
        WorkHour workHour = WorkHour.builder() //zastosowalem tutaj builder pattern o ktorym mowilem wczesniej na zajeciach
                .uuid(UUID.randomUUID().toString())
                .start(request.start())
                .end(request.end())
                .user(loggedUser)
                .build();
        workHourRepository.save(workHour);

    }


    public List<WorkHourWithUserDto> getWorkHoursForAllUsers(LocalDate from, LocalDate to) {
        List<WorkHour> workHoursByStartIsBetween = workHourRepository.getWorkHoursByStartIsBetween(
                LocalDateTime.of(from, LocalTime.MIN),
                LocalDateTime.of(to, LocalTime.MAX));
        return workHoursByStartIsBetween.stream()
                .map(workHour -> WorkHourWithUserDto.builder()
                        .workHourUuid(workHour.getUuid())
                        .workHourStart(workHour.getStart())
                        .workHourEnd(workHour.getEnd())
                        .userUuid(workHour.getUser().getUuid())
                        .firstName((workHour.getUser().getFirstName()))
                        .lastName(workHour.getUser().getLastName())
                        .build())
                //.collect(Collectors.toList())
                .toList();
    }
}
