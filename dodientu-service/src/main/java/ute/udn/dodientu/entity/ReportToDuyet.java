package ute.udn.dodientu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportToDuyet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String createDate;

    private long totalQuality;

    private BigDecimal totalMoneyPost;

    private BigDecimal totalMoneyToTypePost;

    @OneToOne
    @JoinColumn(name = "postId")
    private Post post;
}
