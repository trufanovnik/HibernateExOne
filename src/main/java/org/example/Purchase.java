package org.example;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "purchases")
@NoArgsConstructor
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @Column(name = "price_at_purchase ")
    private double priceAtPurchase;

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", customer=" + customer.getName() +
                ", product=" + product.getTitle() +
                ", purchaseDate=" + purchaseDate +
                ", priceAtPurchase=" + priceAtPurchase +
                '}';
    }
}
