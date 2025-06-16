package product.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import product.api.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
