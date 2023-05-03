package ute.udn.dodientu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ute.udn.dodientu.dto.CustomDTO;
import ute.udn.dodientu.entity.TypePost;
import ute.udn.dodientu.service.TypePostService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/dodientu/v1/typepost")
public class TypePostController {

    @Autowired
    private TypePostService typePostService;

    @GetMapping
    public List<TypePost> findAll(@RequestParam(name = "isDelete", required = false) Boolean isDelete) {
        if (isDelete != null) {
            return typePostService.findAll(isDelete);
        }
        return typePostService.findAll();
    }

    @GetMapping("/{id}")
    public TypePost findById(@PathVariable Long id) {
        return typePostService.findById(id);
    }

    @PostMapping
    public TypePost save(@Valid @RequestBody TypePost typePost) {
        return typePostService.save(typePost);
    }

    @DeleteMapping("/{id}")
    public CustomDTO delete(@PathVariable Long id) {
        return typePostService.delete(id);
    }
}