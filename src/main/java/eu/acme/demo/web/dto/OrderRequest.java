package eu.acme.demo.web.dto;

public class OrderRequest {

    private String clientReferenceCode;

    private OrderDto orderDto;


    public String getClientReferenceCode() {
        return clientReferenceCode;
    }

    public void setClientReferenceCode(String clientReferenceCode) {
        this.clientReferenceCode = clientReferenceCode;
    }


    public OrderDto getOrderDto() {
        return orderDto;
    }

    public void setOrderDto(OrderDto orderDto) {
        this.orderDto = orderDto;
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "clientReferenceCode='" + clientReferenceCode + '\'' +
                ", orderDto=" + orderDto +
                '}';
    }
}


