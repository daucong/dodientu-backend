package ute.udn.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ute.udn.userservice.entity.ERole;
import ute.udn.userservice.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(ERole roleName);
}
