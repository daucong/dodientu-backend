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
public class District extends BaseEntity {

    private String name;

    private String lat;

    private String lng;

    @ManyToOne
    @JoinColumn(name = "provinceId")
    private Province province;

    @OneToMany(mappedBy = "district")
    @JsonIgnore
    private List<Ward> wards;
}