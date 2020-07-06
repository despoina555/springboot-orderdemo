package eu.acme.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.acme.demo.domain.Order;
import eu.acme.demo.domain.enums.OrderStatus;
import eu.acme.demo.repository.OrderItemRepository;
import eu.acme.demo.repository.OrderRepository;
import eu.acme.demo.web.dto.OrderDto;
import eu.acme.demo.web.dto.OrderItemDto;
import eu.acme.demo.web.dto.OrderRequest;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderAPITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    ModelMapper modelMapper;


    @Test
    void testOrderAPI() throws Exception {

        OrderRequest orderRequest = createOrderRequest("ORDER-0");

        MvcResult orderResult = this.mockMvc.perform(post("http://api.okto-demo.eu/orders").
                content(asJsonString(orderRequest)).
                contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        OrderDto orderDto =asObject(orderResult.getResponse().getContentAsString(), OrderDto.class);

        Assert.assertEquals(orderDto.getStatus(), OrderStatus.SUBMITTED);
        Assert.assertEquals(orderDto.getClientReferenceCode(),"ORDER-0");

        //checks order items also
        Order order = modelMapper.map(orderDto, Order.class);
        Assert.assertTrue(orderItemRepository.findByOrder(order).isPresent());
    }

    @Test
    void testOrderDoubleSubmission() throws Exception {
        //write a test to trigger validation error when submit the same order twice (same client reference code)
        OrderRequest orderRequest1 = createOrderRequest("ORDER-1");
        MvcResult orderResult = this.mockMvc.perform(post("http://api.okto-demo.eu/orders").
                content(asJsonString(orderRequest1))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        OrderDto orderDto1 =asObject(orderResult.getResponse().getContentAsString(), OrderDto.class);
        Assert.assertEquals(orderDto1.getStatus(), OrderStatus.SUBMITTED);
        Assert.assertEquals(orderDto1.getClientReferenceCode(),"ORDER-1");

        OrderRequest orderRequest2 = createOrderRequest("ORDER-1");
       this.mockMvc.perform(post("http://api.okto-demo.eu/orders").
                content(asJsonString(orderRequest2))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void testFetchAllOrders() throws Exception {
        // create 2 orders (by directly saving to database) and then invoke API call to fetch all orders
        // check that response contains 2 orders

        //do the Clean Up
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();

        OrderDto orderDto1 =  createOrderDTO("ORDER-1");
        Order order1 = modelMapper.map(orderDto1, Order.class);
        order1.setStatus(OrderStatus.SUBMITTED);
        orderRepository.save(order1);
        OrderDto orderDto2 =   createOrderDTO("ORDER-2");
        Order order2 = modelMapper.map(orderDto2, Order.class);
        order2.setStatus(OrderStatus.SUBMITTED);
        orderRepository.save(order2);


        MockHttpServletResponse response = this.mockMvc.perform(get("http://api.okto-demo.eu/orders"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        List<Order>  orderList =
                objectMapper.readValue(response.getContentAsString(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Order.class));

        assertEquals(2, orderList.size());

    }

    @Test
    void testFetchCertainOrder() throws Exception {
        //create 1 order (by directly saving to database) and then invoke API call to fetch order
        //check response contains the correct order

        OrderDto orderDto1 =  createOrderDTO("ORDER-3");
        Order order1 = modelMapper.map(orderDto1, Order.class);
        order1.setStatus(OrderStatus.SUBMITTED);
        orderRepository.save(order1);

        MockHttpServletResponse response = this.mockMvc.perform(get("http://api.okto-demo.eu/orders/"+order1.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        OrderDto orderDto =asObject(response.getContentAsString(), OrderDto.class);
        assertEquals("ORDER-3", orderDto.getClientReferenceCode());
        assertEquals(order1.getId(), orderDto.getId());
    }


    @Test
    void testFetchCertainOrderThatDoesntExists() throws Exception {
        //write one more test to check that when an order not exists, server responds with http 400

        OrderDto orderDto1 =  createOrderDTO("ORDER-6");
        Order order1 = modelMapper.map(orderDto1, Order.class);
        order1.setStatus(OrderStatus.SUBMITTED);
        orderRepository.save(order1);

         this.mockMvc.perform(get("http://api.okto-demo.eu/orders/"+order1.getId()+1))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    private  OrderRequest createOrderRequest(String clientReferenceCode){
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderDto(createOrderDTO(clientReferenceCode));
        orderRequest.setClientReferenceCode(clientReferenceCode);
        return  orderRequest;
    }


    private OrderDto createOrderDTO(String clientReferenceCode) {
        OrderDto orderDto = new OrderDto();
        OrderItemDto item1=new OrderItemDto();

        item1.setItemId(UUID.randomUUID());
        item1.setUnitPrice(BigDecimal.valueOf(10));
        item1.setUnits(2);
        item1.setTotalPrice(BigDecimal.valueOf(20));
        List<OrderItemDto> itemDtoList= new ArrayList<OrderItemDto>();
        itemDtoList.add(item1);
        orderDto.setOrderItems(itemDtoList);

        orderDto.setClientReferenceCode(clientReferenceCode);
        orderDto.setDescription("first order");
        orderDto.setItemCount(2);
        orderDto.setTotalAmount(BigDecimal.valueOf(20));
        return  orderDto;
    }

    protected String asJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    protected <T> T asObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            return null;
        }
    }
}

