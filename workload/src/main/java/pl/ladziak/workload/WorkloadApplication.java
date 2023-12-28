package pl.ladziak.workload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.ladziak.workload.models.Role;
import pl.ladziak.workload.models.User;
import pl.ladziak.workload.repositories.UserRepository;

import java.util.List;

@SpringBootApplication
public class WorkloadApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(WorkloadApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		User userOne = new User(1L, "firstName", "lastName", "email", "passowrd", Role.USER);
//		User userTwo = new User(2L, "firstName", "lastName", "email", "passowrd", Role.USER);
//		User userThree = new User(3L, "firstName", "lastName", "email", "passowrd", Role.USER);

//		userRepository.saveAll(List.of(userOne, userTwo, userThree));
	}
}
