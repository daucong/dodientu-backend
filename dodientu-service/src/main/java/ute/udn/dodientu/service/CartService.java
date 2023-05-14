package ute.udn.dodientu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ute.udn.dodientu.entity.Cart;
import ute.udn.dodientu.repository.CartRepository;

import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public List<Cart> getAlllist() {
        return cartRepository.findAll();
    }

    public Cart getOneById(Long id) {
        return cartRepository.findById(id).get();
    }

    public Cart saveOrUpdate(Cart entity) {
        return cartRepository.save(entity);
    }

    public boolean delete(Long id) {
        cartRepository.deleteById(id);
        return false;
    }
}