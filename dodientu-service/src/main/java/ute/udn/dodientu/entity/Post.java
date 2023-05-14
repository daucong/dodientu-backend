package ute.udn.dodientu.entity;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
public class Post extends BaseEntity {

    private String title;

    @Lob
    private String description;

    private String thumbnail;

    private String address;

    private BigDecimal price;

    private int quantity;

    private Integer status;

    private int statusPlus;

    private Integer viewCount;

    private BigDecimal postPrice;

    private Long userId;

    private String adminMessage;

    private Timestamp postDate;

    private Timestamp endDate;

    private String emailContact;

    private String sdtContact;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "post",cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PostImage> images;

    @ManyToOne
    @JoinColumn(name = "wardId")
    private Ward ward;

    @ManyToOne
    @JoinColumn(name = "typePostId")
    private TypePost typePost;

}