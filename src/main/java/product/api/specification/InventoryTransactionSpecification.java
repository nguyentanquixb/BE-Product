package product.api.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import product.api.entity.InventoryTransaction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class InventoryTransactionSpecification {

    public static Specification<InventoryTransaction> filterByCriteria(Long productId, Long warehouseId, LocalDate fromDate, LocalDate toDate) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (productId != null) {
                predicates.add(criteriaBuilder.equal(root.get("product").get("id"), productId));
            }

            if (warehouseId != null) {
                predicates.add(criteriaBuilder.equal(root.get("warehouse").get("id"), warehouseId));
            }
            if (fromDate != null && toDate != null) {
                predicates.add(criteriaBuilder.between(
                        root.get("transactionDate"),
                        fromDate.atStartOfDay(),
                        toDate.atTime(LocalTime.MAX)
                ));
            } else if (fromDate != null) {
                predicates.add(criteriaBuilder.between(
                        root.get("transactionDate"),
                        fromDate.atStartOfDay(),
                        fromDate.atTime(LocalTime.MAX)
                ));
            } else if (toDate != null) {
                predicates.add(criteriaBuilder.between(
                        root.get("transactionDate"),
                        toDate.atStartOfDay(),
                        toDate.atTime(LocalTime.MAX)
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

