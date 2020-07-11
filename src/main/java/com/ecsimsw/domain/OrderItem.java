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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;  // FK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order; // FK

    private int orderPrice;

    private int count;

    // 생성자 사용 제한.
    protected OrderItem(){}

    // 생성 메소드 //
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    // 비지니스 로직 //
    public void cancle() {
        getItem().addStock(count);
    }

    // 조회 로직 //
    public int getTotalPrice() {
        return getOrderPrice()*count;
    }
}
