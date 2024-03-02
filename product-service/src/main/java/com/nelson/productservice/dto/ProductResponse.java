package com.nelson.productservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductResponse {
    private long id;
    private String name;
    private long price;
    private long quantity;

}
