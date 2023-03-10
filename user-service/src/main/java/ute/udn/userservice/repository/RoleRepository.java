package com.sdt.userservice.repository;

import com.sdt.userservice.entity.ERole;
import com.sdt.userservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(ERole roleName);
}
