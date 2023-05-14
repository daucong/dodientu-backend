package ute.udn.dodientu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ute.udn.dodientu.dto.MessageDTO;
import ute.udn.dodientu.entity.Cart;
import ute.udn.dodientu.service.CartService;
import ute.udn.dodientu.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/dodientu/v1/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private PostService postService;

    @GetMapping
    public List<Cart> list() {
        return cartService.getAlllist();
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Cart cart) {
        cartService.saveOrUpdate(cart);
        return ResponseEntity.ok(new MessageDTO("Lưu thành công!"));
    }

    @GetMapping("/{id}")
    public Cart findById(@PathVariable("id") Long id) {
        return cartService.getOneById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        cartService.delete(id);
    }
}
