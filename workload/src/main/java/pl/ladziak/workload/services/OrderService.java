package pl.ladziak.workload.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public String createOrder(CreateOrderRequest request) {
        Order order = Order.builder()
                .uuid(UUID.randomUUID().toString())
                .title(request.title())
                .description(request.description())
                .from(request.from())
                .to(request.to())
                .build();

        return orderRepository.save(order).getUuid();

    }

    @Transactional
    public OrderResponse getOrdersFromRange(User user, LocalDate from, LocalDate to) {
        List<Order> results = orderRepository.getOrdersByUsersInAndFromAfterAndFromBefore(
                Set.of(user),
                LocalDateTime.of(from, LocalTime.MIN),
                LocalDateTime.of(to, LocalTime.MAX)
        );

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

    @Transactional
    public List<OrderWithUsersResponse> getAllOrdersWithUsers(User loggedUser, LocalDate from, LocalDate to) {
        List<Order> orders = orderRepository.getOrdersByUsersInAndFromAfterAndFromBefore(
                Set.of(loggedUser),
                LocalDateTime.of(from, LocalTime.MIN),
                LocalDateTime.of(to, LocalTime.MAX)
        );

        return orders.stream()
                .map(order -> OrderWithUsersResponse.builder()
                        .order(OrderDto.builder()
                                .uuid(order.getUuid())
                                .title(order.getTitle())
                                .description(order.getDescription())
                                .from(order.getFrom())
                                .to(order.getTo())
                                .build())
                        .users(order.getUsers().stream()
                                .map(user -> UserDto.builder()
                                        .uuid(user.getUuid())
                                        .firstName(user.getFirstName())
                                        .lastName(user.getLastName())
                                        .role(user.getRole())
                                        .build())
                                .toList())
                        .build())
                .toList();
    }
}
