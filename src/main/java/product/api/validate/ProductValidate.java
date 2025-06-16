package product.api.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import product.api.dto.ProductRequest;
import product.api.entity.ProductStatusEnum;
import product.api.repository.CategoryRepository;
import product.api.repository.SupplierRepository;
import product.api.repository.WarehouseRepository;
import product.api.response.Response;
import product.api.service.ProductService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductValidate {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    public List<String> validateProduct(ProductRequest request) {
        List<String> errors = new ArrayList<>();


        if (request.getName() == null || request.getName().isEmpty()) {
            errors.add("Name is required");
        }
        if (request.getProductCode() == null || request.getProductCode().isEmpty() || request.getProductCode().length() > 50) {
            errors.add("Product Code must be less than 50 characters");
        }
        if (request.getPrice() == null || request.getPrice().doubleValue() < 0) {
            errors.add("Price must be positive");
        }
        if (request.getQuantity() == null || request.getQuantity() < 0) {
            errors.add("Quantity must be positive");
        }
        if (request.getMinStock() == null || request.getMinStock() < 0) {
            errors.add("Min Stock must be positive");
        }
        if (request.getBarcode() == null || request.getBarcode().isEmpty()) {
            errors.add("Barcode is required");
        }
        if (request.getProductCode() == null || request.getProductCode().isEmpty() || request.getProductCode().length() > 50) {
            errors.add(" Product code is empty, invalid, or exceeds 50 characters");
        } else if (productService.isProductCodeDuplicate(request.getProductCode())) {
            errors.add("Product code '" + request.getProductCode() + "' already exists");
        }
        if (request.getCategoryId() == null || !categoryRepository.existsById(request.getCategoryId())) {
            errors.add("Category ID is invalid");
        }
        if (request.getWarehouseId() == null || !warehouseRepository.existsById(request.getWarehouseId())) {
            errors.add("Warehouse ID is invalid");
        }
        if (request.getSupplierId() == null || !supplierRepository.existsById(request.getSupplierId())) {
            errors.add("Supplier ID is invalid");
        }
        try {
            ProductStatusEnum.valueOf(request.getStatus().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            errors.add("Invalid status. Allowed values: ACTIVE, INACTIVE");
        }

        return errors;
    }

    public List<String> validateProductExcel(ProductRequest request, int rowIndex) {
        List<String> errors = new ArrayList<>();

        checkEmpty(request.getName(), "Product name", rowIndex, errors);
        checkEmpty(request.getBarcode(), "Barcode", rowIndex, errors);
        checkPositiveNumber(request.getPrice(), "Price", rowIndex, errors);
        checkPositiveNumber(request.getQuantity(), "Quantity", rowIndex, errors);
        checkPositiveNumber(request.getMinStock(), "MinStock", rowIndex, errors);

        if (request.getCategoryId() == null || !categoryRepository.existsById(request.getCategoryId())) {
            errors.add("Row " + (rowIndex) + ": Category ID is invalid");
        }
        if (request.getWarehouseId() == null || !warehouseRepository.existsById(request.getWarehouseId())) {
            errors.add("Row " + (rowIndex) + ": Warehouse ID is invalid");
        }
        if (request.getSupplierId() == null || !supplierRepository.existsById(request.getSupplierId())) {
            errors.add("Row " + (rowIndex) + ": Supplier ID is invalid");
        }

        if (request.getProductCode() == null || request.getProductCode().isEmpty() || request.getProductCode().length() > 50) {
            errors.add("Row " + (rowIndex) + ": Product code is empty, invalid, or exceeds 50 characters");
        } else if (productService.isProductCodeDuplicate(request.getProductCode())) {
            errors.add("Row " + (rowIndex) + ": Product code '" + request.getProductCode() + "' already exists");
        }

        try {
            ProductStatusEnum.valueOf(request.getStatus().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            errors.add("Row " + (rowIndex) + ": Invalid product status '" + request.getStatus() + "'");
        }

        return errors;
    }

    private void checkEmpty(String value, String fieldName, int rowIndex, List<String> errors) {
        if (value == null || value.isEmpty()) {
            errors.add("Row " + rowIndex + ": " + fieldName + " is empty or invalid");
        }
    }

    private void checkPositiveNumber(Number value, String fieldName, int rowIndex, List<String> errors) {
        if (value == null || value.doubleValue() < 0) {
            errors.add("Row " + rowIndex + ": " + fieldName + " is empty or negative");
        }
    }
}
