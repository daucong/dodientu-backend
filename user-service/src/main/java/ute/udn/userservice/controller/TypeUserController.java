package ute.udn.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ute.udn.userservice.entity.TypeUser;
import ute.udn.userservice.service.TypeUserService;

import java.util.List;

@RestController
@RequestMapping("/api/user/v1/type")
public class TypeUserController {
    @Autowired
    private TypeUserService typeUserService;

    @GetMapping
    public List<TypeUser> list() {
        return typeUserService.getAlllist();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addTypeUser(@RequestBody TypeUser typeUser) {
        typeUserService.saveOrUpdate(typeUser);
    }

    @GetMapping("/{id}")
    public TypeUser findTypeUserId(@PathVariable("id") Long id) {
        return typeUserService.getOneById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteTypeUser(@PathVariable("id") Long id) {
        typeUserService.delete(id);
    }
}
