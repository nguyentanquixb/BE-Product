package product.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import product.api.entity.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
