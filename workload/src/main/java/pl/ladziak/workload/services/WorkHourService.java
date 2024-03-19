package pl.ladziak.workload.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ladziak.workload.dto.WorkHourDto;
import pl.ladziak.workload.dto.WorkHourWithUserDto;
import pl.ladziak.workload.models.User;
import pl.ladziak.workload.models.WorkHour;
import pl.ladziak.workload.repositories.WorkHourRepository;
import pl.ladziak.workload.request.CreateHoursRequest;
import pl.ladziak.workload.response.WorkHourSummaryResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkHourService {
    private final WorkHourRepository workHourRepository;

    public List<WorkHourDto> getWorkHoursCurrentUserByUserId(Long id, LocalDate from, LocalDate to) {

        LocalDateTime fromTime = LocalDateTime.of(from, LocalTime.MIN);
        LocalDateTime toTime = LocalDateTime.of(to, LocalTime.MAX);
        List<WorkHour> workHoursByUserId = workHourRepository.getWorkHoursByUserId(id);
        return workHoursByUserId.stream()
                .filter(workHour -> (workHour.getStart().isAfter(fromTime) || workHour.getStart().equals(fromTime))
                        && (workHour.getStart().isBefore(toTime) || workHour.getStart().isEqual(toTime)))
                .map(workHour -> WorkHourDto.builder()
                        .uuid(workHour.getUuid())
                        .start(workHour.getStart())
                        .end(workHour.getEnd())
                        .accepted(workHour.isAccepted())
                        .build())
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

    @Transactional
    public List<WorkHourSummaryResponse> getSummary(LocalDate from, LocalDate to) {
        List<WorkHour> workHours = workHourRepository.getWorkHoursByStartIsBetween(
                LocalDateTime.of(from, LocalTime.MIN),
                LocalDateTime.of(to, LocalTime.MAX));

        List<User> users = workHours.stream()
                .map(WorkHour::getUser)
                .distinct()
                .toList();

        return users.stream()
                .map(user -> {
                    Long hours = workHours.stream()
                            .filter(workHour -> workHour.getUser().getUuid().equals(user.getUuid()))
                            .map(workHour -> ChronoUnit.HOURS.between(workHour.getStart(), workHour.getEnd()))
                            .reduce(0L, Long::sum);
                    return WorkHourSummaryResponse.builder()
                            .userUuid(user.getUuid())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .workedHours(hours)
                            .build();
                }).toList();
    }

    public List<WorkHourWithUserDto> getWorkHoursForAllUsers(LocalDate from, LocalDate to) {
        List<WorkHour> workHoursByStartIsBetween = workHourRepository.getWorkHoursByStartIsBetween(
                LocalDateTime.of(from, LocalTime.MIN),
                LocalDateTime.of(to, LocalTime.MAX));
        return workHoursByStartIsBetween.stream()
                .filter(workHour -> !workHour.isAccepted())
                .map(workHour -> WorkHourWithUserDto.builder()
                        .workHourUuid(workHour.getUuid())
                        .workHourStart(workHour.getStart())
                        .workHourEnd(workHour.getEnd())
                        .userUuid(workHour.getUser().getUuid())
                        .firstName((workHour.getUser().getFirstName()))
                        .lastName(workHour.getUser().getLastName())
                        .accepted(workHour.isAccepted())
                        .build())
                //.collect(Collectors.toList())
                .toList();
    }

    public void acceptHours(List<String> uuid) {
        List<WorkHour> allByUuidIn = workHourRepository.findAllByUuidIn(uuid);

        allByUuidIn.forEach(workHour -> workHour.setAccepted(true));

        workHourRepository.saveAll(allByUuidIn);
    }
}
