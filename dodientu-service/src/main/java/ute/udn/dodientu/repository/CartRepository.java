package ute.udn.dodientu.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ute.udn.dodientu.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findAllByPostIdAndUserId(Long postId, Long userId);
}
