package eu.acme.demo.web.dto;

import eu.acme.demo.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderLiteDto {
    private UUID id;
    private OrderStatus status;
    private String description;
    /**
     * reference code used by client system to track order
     */
    private String clientReferenceCode;
    private BigDecimal totalAmount;
    private int itemCount;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClientReferenceCode() {
        return clientReferenceCode;
    }

    public void setClientReferenceCode(String clientReferenceCode) {
        this.clientReferenceCode = clientReferenceCode;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
