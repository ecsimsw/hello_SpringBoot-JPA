package com.ecsimsw.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Getter @Setter
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
