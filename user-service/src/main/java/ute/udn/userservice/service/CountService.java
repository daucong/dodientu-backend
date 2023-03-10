package com.sdt.userservice.service;

import com.sdt.userservice.entity.Bank;
import com.sdt.userservice.entity.Count;
import com.sdt.userservice.repository.BankRepository;
import com.sdt.userservice.repository.CountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountService {
    @Autowired
    private CountRepository countRepository;

    public List<Count> getAlllist() {
        return countRepository.findAll();
    }

    public Count getOneById(Long id) {
        return countRepository.findById(id).get();
    }

    public Count saveOrUpdate(Count entity) {
        return countRepository.save(entity);
    }

    public boolean delete(Long id) {
        countRepository.deleteById(id);
        return false;
    }
}
