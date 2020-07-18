package com.ecsimsw.controller;

import com.ecsimsw.domain.item.Book;
import com.ecsimsw.domain.item.Item;
import com.ecsimsw.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form){
        Book book = new Book();
        // book.createBook()으로 setter 다 지우기를 추천
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model){

        model.addAttribute("items",itemService.findItems());

        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){
        Book item = (Book)itemService.findOne(itemId);
        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form",form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form")BookForm form){
        Book book = new Book();

        book.setId(form.getId());
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/items";

        // 수정하고자 하는 아이템은 persist가 아닌 merge 처리한다.

        // 기본적인 변경 감지는 dirty checking으로 이뤄지기 때문에
        // Book book = em.find(Book.class, 1L);
        // book.setName("changed");
        // 이후 transaction commit만 일어나면 알아서 수정된다.

        // 위
        // Book book = new Book();
        // book.setId(form.getId());
        // 의 book처럼 이미 한번 Db에 저장된 기존 식별자를 갖는 엔티티는
        // 영속성 컨텍스트가 더 이상 관리하지 않는다.
        // 이런 엔티티를 준영속 엔티티라고 한다.

        // 이 준영속 엔티티는 JPA가 관리하지 않기 때문에, dirty checking이 일어나지 않는다.

        // 준영속 엔티티를 수정하는 2가지 방법
        // 1. 변경 감지 기능(dirty checking) 사용.
        // 2. merge 사용

        /* 1번 dirtyChecking 사용
         @Transactional
         void update(Item itemParam)
            Item findItem = em.find(Item.class, itemParam.getId());
            // entityManager로 entity를 직접 꺼내, 값을 수정한다.

            findItem.setPrice(itemParam.getPrice());
         */

         /* 2번 merge 사용
         public void save(Item item){
          if(item.getId() == null){  // 아직 영속되지 않았다.
            em.persist(item);
          } else{                    // 이미 존재하는 id 값이다.
            em.merge(item);
          }
         }

         이미 item의 id가 있다면 merge(),
         일단 해당 준영속 엔티티를 1차 캐시에서 먼저 찾는다.
         없으면 db에서 찾아와서 해당 엔티티의 값에 준영속 엔티티의 모든 값을 붙여넣는다.

         주의1)
         위 코드에서 item 자체는 영속성 컨텍스트에 들어가지 않는다.
         영속된 엔티티를 받고 싶다면
         Item = em.merge(item);
         해서 Item을 가져다 써야한다.

         주의2)
         merge는 모든 값을 변경한다.
         만약 item에서 필드 하나를 set하지 않았다면
         기존 db에 값이 있는지 상관없이 null로 처리된다.

         가급적 dirty checking 방식으로 하는 것이 좋다.
         */
    }
}
