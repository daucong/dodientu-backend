package ute.udn.dodientu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ute.udn.dodientu.entity.Province;

import java.util.List;

public interface ProvinceRepository extends JpaRepository<Province, Long> {
    List<Province> findAllByIsDelete(Boolean isDelete);

    Province findByName(String name);
}