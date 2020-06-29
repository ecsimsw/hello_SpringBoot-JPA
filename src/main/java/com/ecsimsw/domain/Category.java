package com.ecsimsw.domain;

import com.ecsimsw.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter @Setter
public class Category {
    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    // 다대다는 joinTable이 필요하다.
    // 결국 테이블이 일대다/ 다대일로 풀어내기 위해 중간 테이블을 하나 더 생성한다.
    // 중간 테이블에는 값을 더 추가할 수 없고 운영이 어려워 실무에선 사용x
    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name="category_id"),
            inverseJoinColumns = @JoinColumn(name="item_id")
    )
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }
}
