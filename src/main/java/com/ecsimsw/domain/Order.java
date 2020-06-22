package com.ecsimsw.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

@Entity
@Table(name = "orders") // db 테이블명 지정. 디폴트는 class 명
@Getter @Setter
public class Order {

    // Id 기본키, db에 의해 자동 생성
    @Id @GeneratedValue
    private Long id;
}
