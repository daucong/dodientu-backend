package ute.udn.dodientu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ute.udn.dodientu.dto.CountPostDTO;
import ute.udn.dodientu.entity.Post;

import java.util.List;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    Page<Post> findAllByIsDelete(Pageable pageable, Boolean isDelete);

    @Query(value =
            "SELECT * FROM post p " +
                    "WHERE p.is_delete = false and p.status = 2 and p.end_date > curdate() " +
                    "and p.title LIKE %:query%",
            countQuery = "SELECT count(*) FROM post ", nativeQuery = true)
    Page<Post> searchByTitle(Pageable pageable, @Param("query") String query);

    @Query(value =
            "SELECT * FROM post p " +
                    "WHERE p.is_delete = false and p.status = 2 and p.end_date > curdate() ",
            countQuery = "SELECT count(*) FROM post ", nativeQuery = true)
    Page<Post> showFindAll(Pageable pageable);

    @Query(value =
            "SELECT * FROM post p " +
                    "WHERE p.is_delete = false and p.status = 2 and p.end_date > curdate() " +
                    "and p.category_id LIKE %:category_id%",
            countQuery = "SELECT count(*) FROM post ", nativeQuery = true)
    Page<Post> findAllByCategory_Id(Pageable pageable, @Param("category_id") Long categoryId);

    Post findByTitle(String title);

    Page<Post> findAllByIsDeleteAndStatus(Pageable pageable, Boolean isDelete, Integer status);

    Page<Post> findAllByIsDeleteAndUserIdAndStatus(Pageable pageable, Boolean isDelete, Long userId, Integer status);

    Page<Post> findAllByWard_District_Province_IdAndIsDeleteAndStatus(Pageable pageable, Long provinceId, Boolean isDelete, Integer status);

    Page<Post> findAllByCategory_IdAndIsDeleteAndStatus(Pageable pageable, Long categoryId, Boolean isDelete, Integer status);

    List<Post> findAllByWardIdAndIsDelete(Long id, Boolean isDelete);

    List<Post> findAllByTypePostIdAndIsDelete(Long id, Boolean isDelete);

    @Query(value = "select province.id as provinceId,province.name as name,province.url_image as image, count(post.id) as quantityPost from post" +
            "    inner JOIN ward on post.ward_id = ward.id" +
            "    inner JOIN district ON ward.district_id = district.id" +
            "    left JOIN province ON district.province_id = province.id" +
            "    group by(province.id)" +
            "    limit 6", nativeQuery = true)
    List<CountPostDTO> countPostByprovince();

    @Query(value = "SELECT YEAR(post.post_date) AS year, MONTH(post.post_date) AS month, province.name, count(post.id) AS total_quantity " +
            "FROM post " +
            "JOIN ward ON post.ward_id = ward.id " +
            "JOIN district ON ward.district_id = district.id " +
            "JOIN province ON district.province_id = province.id " +
            "GROUP BY YEAR(post.post_date), MONTH(post.post_date), province.name", nativeQuery = true)
    List<Object[]> countPostByProvince();

    @Query(value = "SELECT YEAR(post_date) AS year, MONTH(post_date) AS month, count(id) AS total_quantity " +
            "FROM post " +
            "GROUP BY YEAR(post_date), MONTH(post_date);", nativeQuery = true)
    List<Object[]> countPostByMount();

    @Query(value = "SELECT YEAR(post_date) AS year, MONTH(post_date) AS month, category.name, count(post.id) AS total_quantity " +
            "FROM post " +
            "JOIN category ON post.category_id = category.id " +
            "GROUP BY YEAR(post_date), MONTH(post_date), category.name;", nativeQuery = true)
    List<Object[]> countPostByCategory();
}