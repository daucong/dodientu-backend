package ute.udn.dodientu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ute.udn.dodientu.entity.Category;
import ute.udn.dodientu.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAlllist() {
        return categoryRepository.findAll();
    }

    public Category getOneById(Long id) {
        return categoryRepository.findById(id).get();
    }

    public Category saveOrUpdate(Category entity) {
        return categoryRepository.save(entity);
    }

    public boolean delete(Long id) {
        categoryRepository.deleteById(id);
        return false;
    }
}