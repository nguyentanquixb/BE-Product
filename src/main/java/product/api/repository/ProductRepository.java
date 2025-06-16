package product.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import product.api.entity.Product;
import product.api.entity.ProductStatusEnum;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Optional<Product> findByProductCode(String productCode);

    boolean existsProductByProductCode(String productCode);

    Product deleteProductById(Long id);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) like lower(concat('%', :name, '%') ) ")
    List<Product> searchProductByName(String name);

    List<Product> findByWarehouseId(Long warehouseId);

    List<Product> findByWarehouseIdAndStatus(Long warehouse_id, ProductStatusEnum status);

    @Query("SELECT p FROM Product p WHERE p.quantity < p.minStock")
    List<Product> findByQuantityLessThanMinStock();
}
