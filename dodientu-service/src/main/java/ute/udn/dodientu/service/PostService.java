package ute.udn.dodientu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import ute.udn.dodientu.dto.CountPostDTO;
import ute.udn.dodientu.dto.CustomDTO;
import ute.udn.dodientu.dto.PostImageDTO;
import ute.udn.dodientu.entity.Post;
import ute.udn.dodientu.entity.PostImage;
import ute.udn.dodientu.exception.NotFoundException;
import ute.udn.dodientu.repository.PostImageRepository;
import ute.udn.dodientu.repository.PostRepository;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class PostService {

    @Value("${image-path}")
    public String PATH_IMAGE;
    @Value("${image-name-post-folder}")
    public String imageFolderName;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostImageRepository postImageRepository;
    @Autowired
    ResourceBundleMessageSource messageSource;

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Page<Post> findAll(Pageable pageable, Boolean isDelete) {
        return postRepository.findAllByIsDelete(pageable, isDelete);
    }

    public Page<Post> showFindAll(Pageable pageable) {
        return postRepository.showFindAll(pageable);
    }

    public Page<Post> findAllByCategory_Id(Pageable pageable, Long categoryId) {
        return postRepository.findAllByCategory_Id(pageable, categoryId);
    }

    public Page<Post> searchByTitle(Pageable pageable, String query) throws Exception {
        if (Objects.isNull(query) || query.equals("null") || query.isEmpty())
            return postRepository.showFindAll(pageable);
        if (query.length() > 0){
            return postRepository.searchByTitle(pageable, query);
        }

        return null;
    }

    public Page<Post> findAllByProvinceId(Pageable pageable, Boolean isDelete, Integer status, Long provinceId) {
        return postRepository.findAllByWard_District_Province_IdAndIsDeleteAndStatus(pageable, provinceId, isDelete, status);
    }

    public Page<Post> findAllByCategoryId(Pageable pageable, Boolean isDelete, Integer status, Long categoryId) {
        return postRepository.findAllByCategory_IdAndIsDeleteAndStatus(pageable, categoryId, isDelete, status);
    }

    public Page<Post> findAllByStatus(Pageable pageable, Boolean isDelete, Integer status) {
        return postRepository.findAllByIsDeleteAndStatus(pageable, isDelete, status);
    }

    public Page<Post> findAllByUserId(Pageable pageable, Boolean isDelete, Long userId, Integer status) {
        return postRepository.findAllByIsDeleteAndUserIdAndStatus(pageable, isDelete, userId, status);
    }

    public Post findById(Long id) {
        return postRepository.findById(id).get();
    }

    @Transactional
    public Post save(Post entity) {
        return postRepository.save(entity);
    }

    @Transactional
    public Post approve(Long postId, Integer status, String message) {
        if (status == 3 && (message == null)) {
            throw new NotFoundException(messageSource.getMessage("ute.udn.validate.post.adminMessage.notblank", null, Locale.getDefault()));
        }
        Post entity = new Post();
        entity = postRepository.findById(postId).get();
        entity.setStatus(status);
        entity.setAdminMessage(message);
        return postRepository.save(entity);
    }

    public PostImage saveImage(PostImageDTO postImageDTO) {
        PostImage postImage = new PostImage();
        postImage.setNameImage(postImageDTO.getNameImage());
        postImage.setUrlImage(postImageDTO.getUrlImage());
        postImage.setPost(postImageDTO.getPost());
        if (postImageDTO.getId() != null) {
            postImage.setId(postImageDTO.getId());
        }

        return postImageRepository.save(postImage);
    }

    public CustomDTO delete(Long id) {
        if (!postRepository.existsById(id)) {
            throw new NotFoundException(messageSource.getMessage("ute.udn.validate.id.notfound", null, Locale.getDefault()));
        }

        Post post = postRepository.findById(id).get();
        post.setIsDelete(true);
        postRepository.save(post);

        return new CustomDTO(HttpStatus.OK, messageSource.getMessage("ute.udn.message.success", null, Locale.getDefault()));
    }

    public CustomDTO deleteRecycle(Long id) {
        if (!postRepository.existsById(id)) {
            throw new NotFoundException(messageSource.getMessage("ute.udn.validate.id.notfound", null, Locale.getDefault()));
        }
        postRepository.deleteById(id);
        File directoryToDelete = new File(PATH_IMAGE + "//" + imageFolderName + "//" + id);
        FileSystemUtils.deleteRecursively(directoryToDelete);

        return new CustomDTO(HttpStatus.OK, messageSource.getMessage("ute.udn.message.success", null, Locale.getDefault()));
    }

    public List<CountPostDTO> getQuantityPostByProvinceId() {
        return postRepository.countPostByprovince();
    }
}
