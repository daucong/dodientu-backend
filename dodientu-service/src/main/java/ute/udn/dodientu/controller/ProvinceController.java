package ute.udn.dodientu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ute.udn.dodientu.dto.CustomDTO;
import ute.udn.dodientu.entity.Province;
import ute.udn.dodientu.service.ProvinceService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/dodientu/v1/province")
public class ProvinceController {

    @Autowired
    private ProvinceService provinceService;

    @GetMapping
    public List<Province> findAll(@RequestParam(name = "isDelete", required = false) Boolean isDelete) {
        if (isDelete != null) {
            return provinceService.findAll(isDelete);
        }

        return provinceService.findAll();
    }

    @GetMapping("/{id}")
    public Province findById(@PathVariable Long id) {
        return provinceService.findById(id);
    }

    @PostMapping
    public Province save(@Valid @RequestBody Province province) {
        return provinceService.save(province);
    }

    @DeleteMapping("/{id}")
    public CustomDTO delete(@PathVariable Long id) {
        return provinceService.delete(id);
    }
}