package com.nelson.productservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(allocationSize = 1, name = "product_seq", sequenceName = "product_seq")
    private long id;
    private String name;
    private long price;
    private long quantity;

}
