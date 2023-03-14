package ute.udn.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ute.udn.userservice.entity.TransactionHistory;
import ute.udn.userservice.repository.TransactionHistoryRepository;

import java.util.List;

@Service
public class TransactionHistoryService {
    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    public List<TransactionHistory> getAlllist() {
        return transactionHistoryRepository.findAll();
    }

    public TransactionHistory getOneById(Long id) {
        return transactionHistoryRepository.findById(id).get();
    }

    public TransactionHistory saveOrUpdate(TransactionHistory entity) {
        return transactionHistoryRepository.save(entity);
    }

    public boolean delete(Long id) {
        transactionHistoryRepository.deleteById(id);
        return false;
    }
}
