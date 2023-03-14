package ute.udn.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ute.udn.userservice.entity.Bank;
import ute.udn.userservice.repository.BankRepository;

import java.util.List;

@Service
public class BankService {
    @Autowired
    private BankRepository bankRepository;

    public List<Bank> getAlllist() {
        return bankRepository.findAll();
    }

    public Bank getOneById(Long id) {
        return bankRepository.findById(id).get();
    }

    public Bank saveOrUpdate(Bank entity) {
        return bankRepository.save(entity);
    }

    public boolean delete(Long id) {
        bankRepository.deleteById(id);
        return false;
    }
}
