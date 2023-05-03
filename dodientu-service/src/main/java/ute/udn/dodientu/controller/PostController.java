package ute.udn.dodientu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ute.udn.dodientu.dto.CountPostDTO;
import ute.udn.dodientu.dto.CustomDTO;
import ute.udn.dodientu.entity.Post;
import ute.udn.dodientu.service.PostService;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/dodientu/v1/post")
public class PostController {

    @Autowired
    private PostService postService;


    @GetMapping
    public Page<Post> findAllSearch(@RequestParam(name = "isDelete", required = false) Boolean isDelete,
                              @RequestParam(name = "status", required = false) Integer status,
                              @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                              @RequestParam(name = "limit", required = false, defaultValue = "5") Integer limit,
                              @RequestParam(name = "sortName", required = false, defaultValue = "DESC") String sortname,
                              @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortby,
                              @RequestParam(name = "query", defaultValue = "") String query,
                              @RequestParam(name = "provinceId", required = false) Long provinceId,
                              @RequestParam(name = "categoryId", required = false) Long categoryId,
                              @RequestParam(name = "showAll", defaultValue = "") String showAll) throws Exception {

        Sort sort = Sort.by(Sort.Direction.fromString(sortname), sortby);
        Pageable pageable = PageRequest.of(page - 1, limit, sort);

        if (!query.equals("")) {
            return postService.searchByTitle(pageable, query);
        }

        if (categoryId != null) {
            return postService.findAllByCategory_Id(pageable, categoryId);
        }

        return postService.showFindAll(pageable);
    }

    @GetMapping("/admin")
    public Page<Post> findAll(@RequestParam(name = "isDelete", required = false) Boolean isDelete,
                              @RequestParam(name = "status", required = false) Integer status,
                              @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                              @RequestParam(name = "limit", required = false, defaultValue = "5") Integer limit,
                              @RequestParam(name = "sortName", required = false, defaultValue = "DESC") String sortname,
                              @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortby,
                              @RequestParam(name = "query", defaultValue = "") String query,
                              @RequestParam(name = "provinceId", required = false) Long provinceId,
                              @RequestParam(name = "categoryId", required = false) Long categoryId,
                              @RequestParam(name = "showAll", defaultValue = "") String showAll) throws Exception {

        Sort sort = Sort.by(Sort.Direction.fromString(sortname), sortby);
        Pageable pageable = PageRequest.of(page - 1, limit, sort);

        if (provinceId != null) {
            return postService.findAllByProvinceId(pageable, isDelete, status, provinceId);
        }

        if (status != null) {
            return postService.findAllByStatus(pageable, isDelete, status);
        }

        if (isDelete != null) {
            return postService.findAll(pageable, isDelete);
        }


        return postService.showFindAll(pageable);
    }

    @GetMapping("/{id}")
    public Post findById(@PathVariable Long id) {
        return postService.findById(id);
    }

    @GetMapping("/user")
    public Page<Post> findAllByUsers(@RequestParam(name = "isDelete", required = false) Boolean isDelete,
                                     @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                     @RequestParam(name = "limit", required = false, defaultValue = "5") Integer limit,
                                     @RequestParam(name = "status", required = false) Integer status,
                                     @RequestParam(name = "userId", required = false) Long userId) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return postService.findAllByUserId(pageable, isDelete, userId, status);
    }

    @GetMapping("/approve/{id}")
    public Post approve(@PathVariable Long id,
                        @RequestParam(name = "status") Integer status,
                        @RequestParam(name = "message", required = false) String message) {
        return postService.approve(id, status, message);
    }

    @PostMapping
    public Post save(@Valid @RequestBody Post post) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        if (Objects.isNull(post.getId())) {
            post.setCreatedDate(formatter.format(date));
        } else {
            Post postOld = postService.findById(post.getId());
            post.setCreatedDate(postOld.getCreatedDate());
            post.setModifyDate(formatter.format(date));
        }
        return postService.save(post);
    }

    @DeleteMapping("/{id}")
    public CustomDTO delete(@PathVariable Long id) {
        return postService.delete(id);
    }

    @GetMapping("/clean/{id}")
    public CustomDTO deleteRecycle(@PathVariable Long id) {
        return postService.deleteRecycle(id);
    }

    @GetMapping("/quantity-byprovince")
    public List<CountPostDTO> quantityPostByProvinceId() {
        return postService.getQuantityPostByProvinceId();
    }

    @PostMapping("/countAccess")
    public void countAccess(@RequestBody Long id) {
        Post post = postService.findById(id);
        post.setViewCount(post.getViewCount() + 1);
        postService.save(post);
    }

    @PostMapping("/setStatusPlus")
    public void setStatusPlus(@RequestBody Long id) {
        Post post = postService.findById(id);
        post.setStatusPlus(1);
        postService.save(post);
    }
}
