package product.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import product.api.entity.SalesOrder;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
}
