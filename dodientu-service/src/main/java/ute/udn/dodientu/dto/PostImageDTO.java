package ute.udn.dodientu.dto;

import lombok.Data;
import ute.udn.dodientu.entity.Post;

@Data
public class PostImageDTO {

    private Long id;

    private String nameImage;

    private String urlImage;

    private Post post;
}
