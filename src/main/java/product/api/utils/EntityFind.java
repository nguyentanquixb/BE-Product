package product.api.utils;

import org.springframework.stereotype.Component;
import product.api.entity.Category;
import product.api.entity.Supplier;
import product.api.entity.Warehouse;
import product.api.exception.EntityNotFoundException;
import product.api.repository.CategoryRepository;
import product.api.repository.SupplierRepository;
import product.api.repository.WarehouseRepository;


@Component
public class EntityFind {

    private final CategoryRepository categoryRepository;
    private final WarehouseRepository warehouseRepository;
    private final SupplierRepository supplierRepository;

    public EntityFind(CategoryRepository categoryRepository, WarehouseRepository warehouseRepository, SupplierRepository supplierRepository) {
        this.categoryRepository = categoryRepository;
        this.warehouseRepository = warehouseRepository;
        this.supplierRepository = supplierRepository;
    }

    public Category findCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category"));
    }

    public Warehouse findWarehouse(Long id) {
        return warehouseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Warehouse"));
    }

    public Supplier findSupplier(Long id) {
        return supplierRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Supplier"));
    }
}
