package io.npee.springtx.order;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void complete() throws NotEnoughMoneyException {
        //given
        Order order = new Order();
        order.setUsername("정상");

        //when
        orderService.order(order);

        //then
        Order findOrder = orderRepository.findById(order.getId()).get();
        Assertions.assertThat(findOrder.getPayStatus()).isEqualTo("완료");
    }

    @Test
    void runtime_exception() {
        //given
        Order order = new Order();
        order.setUsername("예외");

        //when
        Assertions.assertThatThrownBy(() -> orderService.order(order)).isInstanceOf(RuntimeException.class);

        //then
        Optional<Order> optOrder = orderRepository.findById(order.getId());
        Assertions.assertThat(optOrder.isEmpty()).isTrue();
    }

}