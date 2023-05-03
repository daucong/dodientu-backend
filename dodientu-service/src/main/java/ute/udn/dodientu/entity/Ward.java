package ute.udn.dodientu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity
public class Ward extends BaseEntity {

    private String name;

    @ManyToOne
    @JoinColumn(name = "districtId")
    private District district;

    @OneToMany(mappedBy = "ward")
    @JsonIgnore
    private List<Post> posts;
}