package ute.udn.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ute.udn.userservice.entity.TypeUser;
import ute.udn.userservice.entity.User;

import java.util.List;

public interface TypeUserRepository extends JpaRepository<TypeUser, Long> {
    List<User> findAllById(Long id);
}
