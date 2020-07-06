package eu.acme.demo.web;

import eu.acme.demo.domain.Order;
import eu.acme.demo.domain.OrderItem;
import eu.acme.demo.domain.enums.OrderStatus;
import eu.acme.demo.exception.BadRequestException;
import eu.acme.demo.repository.OrderItemRepository;
import eu.acme.demo.repository.OrderRepository;
import eu.acme.demo.web.dto.OrderDto;
import eu.acme.demo.web.dto.OrderRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderAPI {

    @Autowired
    ModelMapper modelMapper;

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderAPI(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<Order> fetchOrders() {
            //fetch all orders in DB
            return  orderRepository.findAll();
    }

    @GetMapping("/{orderId}")
    @Transactional(readOnly = true)
    public ResponseEntity<OrderDto>  fetchOrder(@PathVariable UUID orderId) {

        try{
            //fetch specific order from DB ,if order id not exists then return an HTTP 400 (bad request)
            // with a proper payload that contains an error code and an error message
            Objects.requireNonNull(orderId, "ID should be provided");
            Order order =  orderRepository.findById(orderId)
                    .orElseThrow(()-> new BadRequestException(orderId.toString()));

            OrderDto orderDto= modelMapper.map(order,OrderDto.class);
            return ResponseEntity.ok(orderDto);
        }catch (BadRequestException ex){
            return ResponseEntity.status(400).build();
        }catch (Exception ex){
            return ResponseEntity.status(500).build();
        }

    }

    @PostMapping
    @Transactional
    public ResponseEntity<OrderDto> submitOrder(@RequestBody OrderRequest orderRequest) {
        //TODO: submit a new order
        // if client reference code already exist then return an HTTP 400 (bad request) with a proper payload that contains an error code and an error message

        try {
            Order order = new Order();
            if (orderRepository.findByClientReferenceCode(orderRequest.getClientReferenceCode()).isPresent()) {
                throw new BadRequestException(orderRequest.getClientReferenceCode().toString());
            }
            order = modelMapper.map(orderRequest.getOrderDto(), Order.class);
            order.setStatus(OrderStatus.SUBMITTED);
            orderRepository.save(order);

            List<OrderItem> orderItemsList =modelMapper.map( orderRequest.getOrderDto().getOrderItems(),
                    new TypeToken<List<OrderItem>>(){}.getType());

            Order finalOrder = order;
            orderItemsList.forEach(orderItem -> {
                orderItem.setOrder(finalOrder);
            });
            orderItemRepository.saveAll(orderItemsList);

            OrderDto orderDto = modelMapper.map(order, OrderDto.class);
            return ResponseEntity.ok(orderDto);

        }catch (BadRequestException ex){
            return ResponseEntity.status(400).build();
        }catch (Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(500).build();
        }

    }

}
