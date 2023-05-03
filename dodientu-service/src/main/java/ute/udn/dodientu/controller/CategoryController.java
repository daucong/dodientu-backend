package ute.udn.dodientu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ute.udn.dodientu.entity.Category;
import ute.udn.dodientu.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/dodientu/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> list() {
        return categoryService.getAlllist();
    }

    @PostMapping
    public void addBank(@RequestBody Category category) {
        categoryService.saveOrUpdate(category);
    }

    @GetMapping("/{id}")
    public Category findCategoryId(@PathVariable("id") Long id) {
        return categoryService.getOneById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable("id") Long id) {
        categoryService.delete(id);
    }
}
