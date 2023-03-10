package com.sdt.userservice.repository;

import com.sdt.userservice.entity.AccountBank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBankRepository extends JpaRepository<AccountBank, Long> {
    AccountBank findAllByEmail(String email);
}
