package ute.udn.dodientu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ute.udn.dodientu.dto.CustomDTO;
import ute.udn.dodientu.dto.PostImageDTO;
import ute.udn.dodientu.entity.PostImage;
import ute.udn.dodientu.service.PostService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/dodientu/v1/postimage")
public class PostImageController {
    @Autowired
    private PostService postService;

    @PostMapping
    public PostImage save(@Valid @RequestBody PostImageDTO postImageDTO) {
        return postService.saveImage(postImageDTO);
    }

    @DeleteMapping("/{id}")
    public CustomDTO delete(@PathVariable Long id) {
        return postService.delete(id);
    }
}
