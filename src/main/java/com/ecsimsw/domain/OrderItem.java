package com.ecsimsw.domain;

import com.ecsimsw.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "oder_item_id")
    private Long id; // FK

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;  // FK

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order; // FK

    private int oderPrice;

    private int count;
}
