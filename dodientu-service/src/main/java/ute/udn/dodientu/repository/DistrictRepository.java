package ute.udn.dodientu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ute.udn.dodientu.entity.District;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long> {
    List<District> findAllByIsDelete(Boolean isDelete);

    List<District> findAllByProvinceIdAndIsDelete(Long id, Boolean isDelete);

    District findByNameAndProvinceId(String name, Long id);
}