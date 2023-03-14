package ute.udn.userservice.entity;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
public class TypeUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
