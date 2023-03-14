package ute.udn.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ute.udn.userservice.entity.AccountBank;
import ute.udn.userservice.repository.AccountBankRepository;

import java.util.List;

@Service
public class AccountBankService {
    @Autowired
    private AccountBankRepository accountBankRepository;

    public List<AccountBank> getAlllist() {
        return accountBankRepository.findAll();
    }

    public AccountBank getOneById(Long id) {
        return accountBankRepository.findById(id).get();
    }

    public AccountBank getAllByEmail(String email) {
        return accountBankRepository.findAllByEmail(email);
    }

    public AccountBank saveOrUpdate(AccountBank entity) {
        return accountBankRepository.save(entity);
    }

    public boolean delete(Long id) {
        accountBankRepository.deleteById(id);
        return false;
    }
}
