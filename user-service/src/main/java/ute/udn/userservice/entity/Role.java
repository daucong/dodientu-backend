package ute.udn.userservice.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private ERole name;
}
