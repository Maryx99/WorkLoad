package pl.ladziak.workload.response;

import lombok.Builder;
import pl.ladziak.workload.dto.OrderDto;
import pl.ladziak.workload.dto.UserDto;

import java.util.List;

@Builder
public record OrderWithUsersResponse(OrderDto order, List<UserDto> users) {
}
