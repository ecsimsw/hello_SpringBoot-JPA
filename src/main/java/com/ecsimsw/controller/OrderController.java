package com.ecsimsw.controller;

import com.ecsimsw.domain.Order;
import com.ecsimsw.domain.item.Item;
import com.ecsimsw.repository.OrderSearch;
import com.ecsimsw.service.ItemService;
import com.ecsimsw.service.MemberService;
import com.ecsimsw.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.ecsimsw.domain.Member;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createFrom(Model model){
        List<Member> members =memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items",items);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId")Long item,
                        @RequestParam("count")int count){
        orderService.order(memberId, item, count);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch")OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);

        model.addAttribute("orders",orders);
        return "order/orderLists";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderid){
        orderService.cancelOrder(orderid);
        return "redirect:/orders";
    }

}
