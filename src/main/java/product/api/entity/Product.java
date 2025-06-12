package product.api.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name="product", schema = "product_api" )
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_code", length = 50, nullable = false, unique = true)
    private String productCode;

    @Column(name = "name", length = 255, nullable = false, unique = true)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", precision = 15, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "quantity",nullable = false)
    private Integer quantity = 0;

    @Column(name = "unit", nullable = false)
    private Long unit;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatusEnum status;


    @Column(name = "createdAt",updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt",updatable = false)
    private LocalDateTime updatedAt;
}

