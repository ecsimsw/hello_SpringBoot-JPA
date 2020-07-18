package com.ecsimsw.service;

import com.ecsimsw.domain.Delivery;
import com.ecsimsw.domain.Member;
import com.ecsimsw.domain.Order;
import com.ecsimsw.domain.OrderItem;
import com.ecsimsw.domain.item.Item;
import com.ecsimsw.repository.ItemRepository;
import com.ecsimsw.repository.MemberRepository;
import com.ecsimsw.repository.OrderRepository;
import com.ecsimsw.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송 정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);

        /*
        castcade 덕분에 order에 persist 한번으로 orderItems랑 delivery에
        order save 적용.

        private List<OrderItem> orderItems = new ArrayList<>();
        @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)

         order가 orderItem, delivery를 관리하기 때문에 castcade 처리 가능.
         */

        return order.getId();
    }

    //취소

    @Transactional
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findOne(orderId);
        order.cancel(); //비즈니스 로직
    }

    /*
     비즈니스 로직을 엔티티가 갖도록 하고,
     service에서는 엔티티에 요청을 위임하는 동작만 수행하는
     패턴을 '도메인 모델 패턴'이라고 한다.

     반대로 서비스 계층에서 비즈니스 모델을 처리하면 transaction script pattern이고 한다.
     */

    //검색
    //public List<Order> findOrders(OderSearch orderSearch)

    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAll(orderSearch);
    }

}
