package ute.udn.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import ute.udn.userservice.entity.RefreshToken;
import ute.udn.userservice.entity.User;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(User user);
}
