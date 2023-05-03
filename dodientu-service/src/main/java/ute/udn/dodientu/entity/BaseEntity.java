package ute.udn.dodientu.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isDelete = Boolean.FALSE;

    private Long createdBy;

    private String createdDate;

    private Long modifyBy;

    private String modifyDate;
}