package ute.udn.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ute.udn.userservice.entity.AccountBank;

public interface AccountBankRepository extends JpaRepository<AccountBank, Long> {
    AccountBank findAllByEmail(String email);
}
