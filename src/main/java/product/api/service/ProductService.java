package product.api.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import product.api.dto.ProductRequest;
import product.api.entity.Product;
import product.api.entity.ProductStatusEnum;
import product.api.exception.NotFoundException;
import product.api.repository.ProductRepository;
import product.api.response.ProductResponse;
import product.api.specification.ProductSpecification;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    final SupplierService supplierService;
    final CategoryService categoryService;
    final WarehouseService warehouseService;

    public ProductService(ProductRepository productRepository, SupplierService supplierService, CategoryService categoryService, WarehouseService warehouseService) {
        this.productRepository = productRepository;
        this.supplierService = supplierService;
        this.categoryService = categoryService;
        this.warehouseService = warehouseService;
    }

    public Optional<Product> getProductById(Long id){
        return productRepository.findById(id);
    }

    public Product findById(Long id){
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND,"Product Not Found"));
    }

    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }

    public Product createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setProductCode(request.getProductCode());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setMinStock(request.getMinStock());
        product.setUnit(request.getUnit());
        product.setBarcode(request.getBarcode());
        product.setStatus(request.getStatus());
        product.setCreatedAt(LocalDateTime.now());

        product.setCategory(categoryService.findCategory(request.getCategoryId()));
        product.setWarehouse(warehouseService.findWarehouse(request.getWarehouseId()));
        product.setSupplier(supplierService.findSupplier(request.getSupplierId()));

        return productRepository.save(product);
    }

    public boolean isProductCodeDuplicate(String productCode) {
        return productRepository.existsProductByProductCode(productCode);
    }

    public Product updateProduct(ProductRequest request, Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND,"Product Not Found"));

        existingProduct.setName(request.getName());
        existingProduct.setProductCode(request.getProductCode());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setQuantity(request.getQuantity());
        existingProduct.setMinStock(request.getMinStock());
        existingProduct.setUnit(request.getUnit());
        existingProduct.setBarcode(request.getBarcode());
        existingProduct.setStatus(request.getStatus());
        existingProduct.setUpdatedAt(LocalDateTime.now());

        existingProduct.setCategory(categoryService.findCategory(request.getCategoryId()));
        existingProduct.setWarehouse(warehouseService.findWarehouse(request.getWarehouseId()));
        existingProduct.setSupplier(supplierService.findSupplier(request.getSupplierId()));

        return productRepository.save(existingProduct);

    }

    public Product updateProductList(ProductRequest request) {
        Product existingProduct = productRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND,"Product Not Found"));

        existingProduct.setName(request.getName());
        existingProduct.setProductCode(request.getProductCode());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setQuantity(request.getQuantity());
        existingProduct.setMinStock(request.getMinStock());
        existingProduct.setUnit(request.getUnit());
        existingProduct.setBarcode(request.getBarcode());
        existingProduct.setStatus(request.getStatus());
        existingProduct.setUpdatedAt(LocalDateTime.now());

        existingProduct.setCategory(categoryService.findCategory(request.getCategoryId()));
        existingProduct.setWarehouse(warehouseService.findWarehouse(request.getWarehouseId()));
        existingProduct.setSupplier(supplierService.findSupplier(request.getSupplierId()));

        return productRepository.save(existingProduct);

    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Page<Product> getProductByPage(PageRequest  pageRequest) {
        return productRepository.findAll(pageRequest);
    }

    public List<Product> searchProductByName(String name){
        return productRepository.searchProductByName(name);
    }

    public List<ProductResponse> createProducts(List<ProductRequest> productRequests) {
        List<ProductResponse> savedProducts = new ArrayList<>();

        for (ProductRequest request : productRequests) {
            Product product = new Product();
            product.setName(request.getName());
            product.setProductCode(request.getProductCode());
            product.setDescription(request.getDescription());
            product.setPrice(request.getPrice());
            product.setQuantity(request.getQuantity());
            product.setMinStock(request.getMinStock());
            product.setUnit(request.getUnit());
            product.setBarcode(request.getBarcode());
            product.setStatus(request.getStatus());
            product.setCreatedAt(LocalDateTime.now());

            product.setCategory(categoryService.findCategory(request.getCategoryId()));
            product.setWarehouse(warehouseService.findWarehouse(request.getWarehouseId()));
            product.setSupplier(supplierService.findSupplier(request.getSupplierId()));

            Product saved = productRepository.save(product);
            savedProducts.add(ProductResponse.convertProduct(saved));
        }
        return savedProducts;
    }

    public Page<Product> searchProducts(
            String nameOrCode,
            Long categoryId,
            Long warehouseId,
            Long supplierId,
            ProductStatusEnum status,
            int page,
            int size
    ) {
        Specification<Product> spec = ProductSpecification.filterByCriteria(
                nameOrCode,
                categoryId,
                warehouseId,
                supplierId,
                status
        );

        PageRequest pageRequest = PageRequest.of(page, size);
        return productRepository.findAll(spec, pageRequest);
    }
}
