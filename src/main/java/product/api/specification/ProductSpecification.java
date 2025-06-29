package product.api.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import product.api.entity.Product;
import product.api.entity.ProductStatusEnum;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> filterByCriteria(
            String nameOrCode,
            Long categoryId,
            Long warehouseId,
            Long supplierId,
            ProductStatusEnum status
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nameOrCode != null && !nameOrCode.isEmpty()) {
                Predicate namePredicate = cb.like(cb.lower(root.get("name")), "%" + nameOrCode.toLowerCase() + "%");
                Predicate codePredicate = cb.like(cb.lower(root.get("productCode")), "%" + nameOrCode.toLowerCase() + "%");
                predicates.add(cb.or(namePredicate, codePredicate));
            }

            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            if (warehouseId != null) {
                predicates.add(cb.equal(root.get("warehouse").get("id"), warehouseId));
            }

            if (supplierId != null) {
                predicates.add(cb.equal(root.get("supplier").get("id"), supplierId));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
