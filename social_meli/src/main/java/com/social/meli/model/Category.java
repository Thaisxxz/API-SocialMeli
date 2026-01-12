package com.social.meli.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Category {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        private String name;

        @Column(nullable = false)
        private String description;

        @Column(nullable = false)
        private Boolean active = true;

        @OneToMany(mappedBy = "category")
        private List<Product> products;

}
