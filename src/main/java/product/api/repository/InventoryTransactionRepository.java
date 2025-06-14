package product.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import product.api.entity.InventoryTransaction;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
}
