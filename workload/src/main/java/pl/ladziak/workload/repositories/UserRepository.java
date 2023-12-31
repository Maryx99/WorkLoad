package pl.ladziak.workload.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.ladziak.workload.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByFirstName(String firstName);

//    @Query(nativeQuery = true, value = "select * from app_users where u.id = :id")
//    Optional<User> findByIdNativeQuery(Long id);

    // metoda do wyciagania usera po emailu
    Optional<User> findByEmail(String email);
}
