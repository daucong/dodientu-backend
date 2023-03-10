package com.sdt.userservice.repository;

import com.sdt.userservice.entity.TypeUser;
import com.sdt.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TypeUserRepository extends JpaRepository<TypeUser, Long> {
    List<User> findAllById(Long id);
}
