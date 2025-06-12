package product.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import product.api.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
