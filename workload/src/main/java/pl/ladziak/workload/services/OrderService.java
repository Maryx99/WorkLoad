package pl.ladziak.workload.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ladziak.workload.dto.OrderDto;
import pl.ladziak.workload.dto.UserDto;
import pl.ladziak.workload.models.Order;
import pl.ladziak.workload.models.User;
import pl.ladziak.workload.repositories.OrderRepository;
import pl.ladziak.workload.repositories.UserRepository;
import pl.ladziak.workload.request.CreateOrderRequest;
import pl.ladziak.workload.response.OrderResponse;
import pl.ladziak.workload.response.OrderWithUsersResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public void createOrder(CreateOrderRequest request) {
        Order order = Order.builder()
                .uuid(UUID.randomUUID().toString())
                .title(request.title())
                .description(request.description())
                .from(request.from())
                .to(request.to())
                .build();

        orderRepository.save(order);
    }

    public OrderResponse getOrdersFromRange(User user, LocalDate from, LocalDate to) {
        List<Order> results = orderRepository.getOrdersByUsersInAndFromAfterAndToBefore(Set.of(user), from, to);
        List<OrderDto> list = results.stream()
                .map(result -> OrderDto.builder()
                        .uuid(result.getUuid())
                        .title(result.getTitle())
                        .description(result.getDescription())
                        .from(result.getFrom())
                        .to(result.getTo())
                        .build())
                .toList();

        return OrderResponse.builder()
                .orders(list)
                .user(new UserDto(user.getUuid(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole()))
                .build();

    }

    public void assignUserToOrder(String uuid, List<String> userUuids) {
        Order order = orderRepository.findByUuid(uuid)
                .orElseThrow();
        Set<User> users = userRepository.findAllByUuidIn(userUuids);
        
        order.setUsers(users);
        orderRepository.save(order);
    }

    public OrderWithUsersResponse getOrderWithAssignedUsers(String uuid) {
        Order order = orderRepository.findByUuidAndAssignedUsers(uuid)
                .orElseThrow();

        return OrderWithUsersResponse.builder()
                .order(OrderDto.builder()
                        .uuid(order.getUuid())
                        .title(order.getTitle())
                        .description(order.getDescription())
                        .from(order.getFrom())
                        .to(order.getTo())
                        .build())
                .users(order.getUsers()
                        .stream()
                        .map(user -> new UserDto(user.getUuid(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole()))
                        .toList())
                .build();
    }
}
