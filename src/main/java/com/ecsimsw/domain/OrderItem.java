package com.ecsimsw.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "oder_item_id")
    private Long id; // FK

    private Item item;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order; // FK

    private int oderPrice;

    private int count;
}
