package ute.udn.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ute.udn.userservice.entity.Bank;

public interface BankRepository extends JpaRepository<Bank, Long> {
}
