package product.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import product.api.entity.StockAdjustment;

import java.util.List;

@Repository
public interface StockAdjustmentRepository extends JpaRepository<StockAdjustment, Long> {
    List<StockAdjustment> findAByProductId(Long productId);
}
