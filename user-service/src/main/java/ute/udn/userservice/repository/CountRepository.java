package com.sdt.userservice.repository;

import com.sdt.userservice.entity.Count;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountRepository extends JpaRepository<Count, Long> {
}
