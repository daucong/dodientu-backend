package ute.udn.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ute.udn.userservice.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserNameOrEmail(String usernameOrEmail, String emailOrUsername);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

    User findByUserName(String username);

    User findByEmail(String email);

    User findByResetPasswordToken(String token);

    @Query("SELECT u.emailVerified FROM User u WHERE u.email = ?1")
    Boolean findEmailVerifiedByEmail(String email);
}
