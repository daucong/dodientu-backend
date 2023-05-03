package ute.udn.dodientu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity
public class Province extends BaseEntity {

    private String name;

    private String urlImage;

    private String lat;

    private String lng;

    @OneToMany(mappedBy = "province")
    @JsonIgnore
    private List<District> districts;
}