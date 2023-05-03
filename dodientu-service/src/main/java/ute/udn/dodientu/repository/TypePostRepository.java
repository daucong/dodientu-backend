package ute.udn.dodientu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ute.udn.dodientu.entity.TypePost;

import java.util.List;

public interface TypePostRepository extends JpaRepository<TypePost, Long> {
    List<TypePost> findAllByIsDelete(Boolean isDelete);

    TypePost findByTypePostName(String name);
}