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
@Table(name="product", schema = "product_liquibase" )
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
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
    private String unit;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatusEnum status;

    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at",updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "barcode", length = 100)
    private String barcode;

    @Column(name = "min_stock", nullable = false)
    private Integer minStock;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;
}

