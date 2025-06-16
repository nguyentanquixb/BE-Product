package product.api.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import product.api.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> filterByCriteria(String keyword, Long categoryId, Long warehouseId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isEmpty()) {
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
                Predicate codePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("productCode")), "%" + keyword.toLowerCase() + "%");
                predicates.add(criteriaBuilder.or(namePredicate, codePredicate));
            }

            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("categoryId"), categoryId));
            }

            if (warehouseId != null) {
                predicates.add(criteriaBuilder.equal(root.get("warehouseId"), warehouseId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

