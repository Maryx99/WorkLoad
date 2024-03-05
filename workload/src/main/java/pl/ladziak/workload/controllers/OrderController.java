package pl.ladziak.workload.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.ladziak.workload.models.User;
import pl.ladziak.workload.request.CreateOrderRequest;
import pl.ladziak.workload.response.CreationSucessResponse;
import pl.ladziak.workload.response.OrderResponse;
import pl.ladziak.workload.response.OrderWithUsersResponse;
import pl.ladziak.workload.services.OrderService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CreationSucessResponse> createOrder(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(new CreationSucessResponse(orderService.createOrder(request)));
    }

    @GetMapping
    public ResponseEntity<OrderResponse> getOrdersFromRangeForLoggedUser(@AuthenticationPrincipal User loggedUser, @RequestParam LocalDate from, @RequestParam LocalDate to) {
        return  ResponseEntity.ok(orderService.getOrdersFromRange(loggedUser,from, to));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<OrderWithUsersResponse> getOrderWithAssignedUsers(@PathVariable String uuid) {
        return ResponseEntity.ok(orderService.getOrderWithAssignedUsers(uuid));
    }


    @PutMapping("/{uuid}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void assignUserToOrder(@PathVariable String uuid,@RequestBody List<String> userUuids) {
        orderService.assignUserToOrder(uuid, userUuids);
    }
}
