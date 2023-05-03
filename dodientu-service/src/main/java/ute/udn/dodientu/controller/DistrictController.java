package ute.udn.dodientu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ute.udn.dodientu.dto.CustomDTO;
import ute.udn.dodientu.entity.District;
import ute.udn.dodientu.service.DistrictService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/dodientu/v1/district")
public class DistrictController {

    @Autowired
    DistrictService districtService;

    @GetMapping
    public List<District> findAll(@RequestParam(name = "isDelete", required = false) Boolean isDelete) {
        if (isDelete != null) {
            return districtService.findAll(isDelete);
        }

        return districtService.findAll();
    }

    @GetMapping("/{id}")
    public District findById(@PathVariable Long id) {
        return districtService.findById(id);
    }

    @GetMapping("/province/{id}")
    public List<District> getAllByProvinceID(@PathVariable Long id) {
        return districtService.findAllByProvinceID(id);
    }

    @PostMapping
    public District save(@Valid @RequestBody District district) {
        return districtService.save(district);
    }

    @DeleteMapping("/{id}")
    public CustomDTO delete(@PathVariable Long id) {
        return districtService.delete(id);
    }
}