package product.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import product.api.entity.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductCode(String productCode);

    boolean existsProductByProductCode(String productCode);

    Product deleteProductById(Long id);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) like lower(concat('%', :name, '%') ) ")
    List<Product> searchProductByName(String name);
}
