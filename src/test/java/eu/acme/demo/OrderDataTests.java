package eu.acme.demo;


import eu.acme.demo.domain.Order;
import eu.acme.demo.domain.OrderItem;
import eu.acme.demo.domain.enums.OrderStatus;
import eu.acme.demo.repository.OrderItemRepository;
import eu.acme.demo.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Transactional
public class OrderDataTests {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Test
    public void testCreateOrder() {
        Order o = new Order();
        o.setStatus(OrderStatus.SUBMITTED);
        o.setClientReferenceCode("ORDER-1");
        o.setDescription("first order");
        o.setItemCount(10);
        o.setTotalAmount(BigDecimal.valueOf(100.23));
        orderRepository.save(o);

        Assert.isTrue(orderRepository.findById(o.getId()).isPresent(), "order not found");
        Assert.isTrue(!orderRepository.findById(UUID.randomUUID()).isPresent(), "non existing order found");

        //tests for order items

        OrderItem item1=new OrderItem();
        item1.setUnitPrice(BigDecimal.valueOf(10));
        item1.setUnits(2);
        item1.setTotalPrice(BigDecimal.valueOf(20));
        item1.setOrder(o);
        orderItemRepository.save(item1);

        org.junit.Assert.assertEquals(Optional.of(item1), orderItemRepository.findById(item1.getId()));
        org.junit.Assert.assertEquals(o ,  orderItemRepository.findById(item1.getId()).get().getOrder());

        }

}
