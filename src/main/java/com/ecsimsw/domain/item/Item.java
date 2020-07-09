package com.ecsimsw.domain.item;

import com.ecsimsw.domain.Category;
import com.ecsimsw.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Id;

import javax.persistence.*;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //== 비지니스 로직 ==/

    // setter로 밖에서 값을 계산해서 entity에 set하는거 보다 안정적이고 객체 지향적이다.

    // 제고 수량 증가
    public void addStock(int stockQuantity){
        this.stockQuantity += stockQuantity;
    }

    // 제고 수량 감소
    public void removeStock(int stockQuantity) {
        int remain = this.stockQuantity - stockQuantity;

        if (remain < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = remain;
    }
}
