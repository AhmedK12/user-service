package com.healthaiharbor.ai.userservice.modal;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles = new ArrayList<>();

}
