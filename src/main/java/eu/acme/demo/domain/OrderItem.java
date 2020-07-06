package eu.acme.demo.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem extends AuditableEntity {

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "unit_price", columnDefinition = "DECIMAL(9,2)", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "units", nullable = false)
    private int units;

    @Column(name = "total_price", columnDefinition = "DECIMAL(9,2)", nullable = false)
    private BigDecimal totalPrice;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
