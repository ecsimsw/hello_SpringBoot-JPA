package com.ecsimsw.service;

import com.ecsimsw.domain.Address;
import com.ecsimsw.domain.Order;
import com.ecsimsw.domain.OrderStatus;
import com.ecsimsw.domain.item.Book;
import com.ecsimsw.domain.item.Item;
import com.ecsimsw.domain.Member;
import com.ecsimsw.exception.NotEnoughStockException;
import com.ecsimsw.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
        Member member = createMember();
        Book book = createBook("ecsimsw", 10000, 10);

        int orderCount =2;
        Long orderId = orderService.order(member.getId(),book.getId(),orderCount);

        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문 상품 수는 1", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 10000", 10000*orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량 만큼 재고가 준다.", 8, book.getStockQuantity());

    }

    @Test
    public void 주문취소() throws Exception{
        Member member = createMember();
        Book item = createBook("ecsimsw", 10000, 10);
        int orderCount =2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        orderService.cancelOrder(orderId);

        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문 상태는 cancel", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("재고는 초기 상태 10",10, item.getStockQuantity());

    }

    @Test(expected = NotEnoughStockException.class)
    public void 재고수량초과() throws Exception{
        Member member = createMember();
        Item item = createBook("ecsimsw", 10000, 10);

        int orderCount = 11;

        orderService.order(member.getId(), item.getId(),orderCount);

        fail("재고 수량 부족 예외 발생");
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원A");
        member.setAddress(new Address("CityA", "StreetA","123-123"));
        em.persist(member);
        return member;
    }

}