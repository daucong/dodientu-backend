package ute.udn.dodientu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ute.udn.dodientu.entity.Ward;

import java.util.List;

public interface WardRepository extends JpaRepository<Ward, Long> {
    List<Ward> findAllByIsDelete(Boolean isDelete);

    List<Ward> findAllByDistrictIdAndIsDelete(Long id, Boolean isDelete);

    Ward findByNameAndDistrictId(String name, Long id);
}