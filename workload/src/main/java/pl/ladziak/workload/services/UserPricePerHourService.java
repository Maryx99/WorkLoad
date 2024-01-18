package pl.ladziak.workload.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ladziak.workload.models.User;
import pl.ladziak.workload.models.UserPricePerHourHistory;
import pl.ladziak.workload.repositories.UserPricePerHourRepository;
import pl.ladziak.workload.repositories.UserRepository;
import pl.ladziak.workload.request.CreateNewUserPricePerHour;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserPricePerHourService {
    private final UserPricePerHourRepository userPricePerHourRepository;
    private final UserRepository userRepository;

    public void createNewUserPricePerHour(String userUuid, CreateNewUserPricePerHour request) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow();//todo: zrobic wyjatek
        if(!user.isHired()){
            throw new RuntimeException(); //todo: dodac wyjatek
        }

        UserPricePerHourHistory userPricePerHourHistory = UserPricePerHourHistory.builder()
                .uuid((UUID.randomUUID().toString()))
                .isActive(true)
                .hourlyRate(request.hourlyRate())
                .validFrom(LocalDate.now())
                .user(user)
                .build();

        userPricePerHourRepository.findByIsActiveTrue()
                .ifPresentOrElse(
                        pricePerHourToDeactivate -> {
                            userPricePerHourRepository.save(pricePerHourToDeactivate.toBuilder()
                                    .isActive(false)
                                    .validTo(LocalDate.now())
                                    .build());
                            userPricePerHourRepository.save(userPricePerHourHistory);
                        },
                        () -> userPricePerHourRepository.save(userPricePerHourHistory)
                );
    }
}
