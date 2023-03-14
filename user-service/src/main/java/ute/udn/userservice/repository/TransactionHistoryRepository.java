package ute.udn.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ute.udn.userservice.entity.TransactionHistory;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
}
