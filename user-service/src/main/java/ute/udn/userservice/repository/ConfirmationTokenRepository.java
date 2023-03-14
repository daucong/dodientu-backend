package ute.udn.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ute.udn.userservice.entity.ConfirmationToken;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    ConfirmationToken findByConfirmationToken(String token);
}
