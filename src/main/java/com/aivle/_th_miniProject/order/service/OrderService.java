package com.aivle._th_miniProject.order.service;

import com.aivle._th_miniProject.book.entity.Book;
import com.aivle._th_miniProject.book.repository.BookRepository;
import com.aivle._th_miniProject.order.dto.OrderCreateRequest;
import com.aivle._th_miniProject.order.dto.OrderResponse;
import com.aivle._th_miniProject.order.entity.Order;
import com.aivle._th_miniProject.order.entity.OrderItem;
import com.aivle._th_miniProject.order.repository.OrderRepository;
import com.aivle._th_miniProject.user.User;
import com.aivle._th_miniProject.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public OrderResponse createOrder(OrderCreateRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Order order = Order.builder()
                .user(user)
                .status(Order.OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .totalAmount(0)
                .build();

        Integer total = 0;

        for (OrderCreateRequest.OrderItemRequest itemRequest : request.getItems()) {

            Book book = bookRepository.findById(itemRequest.getBookId())
                    .orElseThrow(() -> new IllegalArgumentException("도서를 찾을 수 없습니다."));

            Integer quantity = itemRequest.getQuantity();
            if(book.getStock() < quantity) {
                throw new IllegalArgumentException("재고가 부족한 상품은 주문할 수 없습니다.");
            }

            book.setStock(book.getStock() - quantity);
            bookRepository.save(book);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .book(book)
                    .quantity(quantity)
                    .price(book.getPrice())
                    .build();

            order.getItems().add(orderItem);

            total += book.getPrice() * quantity;
        }

        order.setTotalAmount(total);

        orderRepository.save(order);
        return OrderResponse.from(order);
    }

    public OrderResponse payOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 정보가 존재하지 않습니다."));

        if (order.getStatus() == Order.OrderStatus.CANCELLED)
            throw new IllegalStateException("취소된 주문입니다.");
        else if (order.getStatus() == Order.OrderStatus.PAID)
            throw new IllegalStateException("이미 결제 완료된 주문입니다.");

        order.setStatus(Order.OrderStatus.PAID);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        return OrderResponse.from(order);
    }

    public OrderResponse cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 정보가 존재하지 않습니다."));

        if (order.getStatus() == Order.OrderStatus.PAID)
            throw new IllegalStateException("결제 완료된 주문은 취소 불가합니다.");
        else if (order.getStatus() == Order.OrderStatus.CANCELLED)
            throw new IllegalStateException("이미 취소된 주문입니다.");

        for (OrderItem item : order.getItems()) {
            Book book = item.getBook();
            book.setStock(book.getStock() + item.getQuantity());
            bookRepository.save(book);
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        return OrderResponse.from(order);
    }

    public OrderResponse getOrderDetail(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 정보가 존재하지 않습니다."));

        return OrderResponse.from(order);
    }

    public List<OrderResponse> getOrdersByUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));

        List<Order> orders = orderRepository.findByUser(user);

        return orders.stream().map(OrderResponse::from).toList();
    }

}
