package com.ecsimsw.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    protected Address(){

    }

    public Address(String city, String street, String zipcode){
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}

// 값 타입은 변경 불가능해야하기 때문에 setter는 제거하고 초기화 시 값으로 고정된 클래스를 만들어야한다.
// JPA 스펙상 기본 생성자를 설정해야하므로 protected로 해서
// 빈 생성자를 쉽게 접근하지 못하도록 막는다.