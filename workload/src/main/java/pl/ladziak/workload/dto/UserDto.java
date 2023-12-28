package pl.ladziak.workload.dto;

import lombok.Builder;
import pl.ladziak.workload.models.Role;

public record UserDto(Long id, String firstName, String lastName, String email, Role role) {
}
