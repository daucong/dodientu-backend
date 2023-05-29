package ute.udn.dodientu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ute.udn.dodientu.dto.*;
import ute.udn.dodientu.entity.Post;
import ute.udn.dodientu.entity.ReportToDuyet;
import ute.udn.dodientu.entity.ReportUserToPayment;
import ute.udn.dodientu.repository.PostRepository;
import ute.udn.dodientu.service.PostService;

import javax.validation.Valid;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/dodientu/v1/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

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
        Page<Post> data = postService.showFindAll(pageable);
        int size = data.getContent().size();
        for (int i = 0; i < size; i++) {
            Long item = data.getContent().get(i).getId();
            Post post = postService.findById(item);
            if (post.getQuantity() == 0) {
                post.setIsDelete(true);
                postService.save(post);
            }
        }
        return data;
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

    @GetMapping("/detail")
    public Post findByTitle(@RequestParam String title) {
        return postService.findByTitle(title);
    }

    @GetMapping("/reportToDuyet")
    public List<ReportToDuyet> findByReportToDuyet() {
        return postService.getAllReportToDuyet();
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
            post.setStatusPlus(0);
        } else {
            Post postOld = postService.findById(post.getId());
            post.setIsDelete(false);
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

    @PostMapping("/subtractCheckout")
    public ResponseEntity<?> subtractCheckout(@RequestBody SubtractCheckoutDTO subtractCheckoutDTO) {
        Post post = postService.findById(subtractCheckoutDTO.getPostId());
        if (post.getQuantity() < subtractCheckoutDTO.getQuality()) {
            return ResponseEntity.ok(new MessageDTO("Số lượng hiện tại không đáp ứng nhu cầu"));
        }
        post.setQuantity(post.getQuantity() - subtractCheckoutDTO.getQuality());
        postService.save(post);
        return ResponseEntity.ok(new MessageDTO("Lưu thành công"));
    }

    @PostMapping("/plusCheckout")
    public ResponseEntity<?> plusCheckout(@RequestBody SubtractCheckoutDTO subtractCheckoutDTO) {
        Post post = postService.findById(subtractCheckoutDTO.getPostId());
        post.setQuantity(post.getQuantity() + subtractCheckoutDTO.getQuality());
        if (!post.getIsDelete()) {
            post.setIsDelete(false);
        }
        if (post.getStatus() == 4) {
            post.setStatus(2);
        }
        postService.save(post);
        return ResponseEntity.ok(new MessageDTO("Lưu thành công"));
    }

    @GetMapping("/post-by-province")
    public ResponseEntity<?> countPostByProvince() {
        List<Object[]> results = postRepository.countPostByProvince();
        List<PostByProvinceDTO> postByProvinces = new ArrayList<>();
        for (Object[] result : results) {
            int year = (int) result[0];
            int month = (int) result[1];
            String provinceName = (String) result[2];
            BigInteger totalQuantity = (BigInteger) result[3];
            postByProvinces.add(new PostByProvinceDTO(year, month, provinceName, totalQuantity.longValue()));
        }
        return ResponseEntity.ok(postByProvinces);
    }

    @GetMapping("/post-by-mounth")
    public ResponseEntity<?> countPostByMounth() {
        List<Object[]> results = postRepository.countPostByMount();
        List<PostByMounthDTO> postByMounts = new ArrayList<>();
        for (Object[] result : results) {
            int year = (int) result[0];
            int month = (int) result[1];
            BigInteger totalQuantity = (BigInteger) result[2];
            postByMounts.add(new PostByMounthDTO(year, month, totalQuantity.longValue()));
        }
        return ResponseEntity.ok(postByMounts);
    }

}
