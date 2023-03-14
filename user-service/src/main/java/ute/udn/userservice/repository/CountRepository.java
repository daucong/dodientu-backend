package ute.udn.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ute.udn.userservice.entity.Count;

public interface CountRepository extends JpaRepository<Count, Long> {
}
