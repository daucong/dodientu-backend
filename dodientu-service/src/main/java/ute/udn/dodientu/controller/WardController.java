package ute.udn.dodientu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ute.udn.dodientu.dto.CustomDTO;
import ute.udn.dodientu.entity.Ward;
import ute.udn.dodientu.service.WardService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/dodientu/v1/ward")
public class WardController {

    @Autowired
    private WardService wardService;

    @GetMapping
    public List<Ward> findAll(@RequestParam(name = "isDelete", required = false) Boolean isDelete) {
        if (isDelete != null) {
            return wardService.findAll(isDelete);
        }

        return wardService.findAll();
    }

    @GetMapping("/{id}")
    public Ward findById(@PathVariable Long id) {
        return wardService.findById(id);
    }

    @GetMapping("/district/{id}")
    public List<Ward> getAllByProvinceID(@PathVariable Long id) {
        return wardService.findAllByDistrictID(id);
    }

    @PostMapping
    public Ward save(@Valid @RequestBody Ward ward) {
        return wardService.save(ward);
    }

    @DeleteMapping("/{id}")
    public CustomDTO delete(@PathVariable Long id) {
        return wardService.delete(id);
    }
}