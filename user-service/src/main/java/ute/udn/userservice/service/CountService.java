package ute.udn.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ute.udn.userservice.entity.Count;
import ute.udn.userservice.repository.CountRepository;

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
