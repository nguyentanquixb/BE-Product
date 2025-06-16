package product.api.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import product.api.dto.ProductRequest;
import product.api.entity.Product;
import product.api.entity.ProductStatusEnum;
import product.api.exception.EntityNotFoundException;
import product.api.repository.ProductRepository;
import product.api.response.ProductResponse;
import product.api.response.Response;
import product.api.utils.EntityFind;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    private  EntityFind entityFind;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;

    }

    public Optional<Product> getProductById(Long id){
        return productRepository.findById(id);
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
        product.setStatus(ProductStatusEnum.valueOf(request.getStatus().trim().toUpperCase()));
        product.setCreatedAt(LocalDateTime.now());

        product.setCategory(entityFind.findCategory(request.getCategoryId()));
        product.setWarehouse(entityFind.findWarehouse(request.getWarehouseId()));
        product.setSupplier(entityFind.findSupplier(request.getSupplierId()));

        return productRepository.save(product);
    }

    public Optional<Product> findProductByProductCode(String productCode){
        return productRepository.findByProductCode(productCode);
    }

    public boolean isProductCodeDuplicate(String productCode) {
        return productRepository.existsProductByProductCode(productCode);
    }

    public Product updateProduct(ProductRequest request) {
        Product existingProduct = productRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        existingProduct.setName(request.getName());
        existingProduct.setProductCode(request.getProductCode());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setQuantity(request.getQuantity());
        existingProduct.setMinStock(request.getMinStock());
        existingProduct.setUnit(request.getUnit());
        existingProduct.setBarcode(request.getBarcode());
        existingProduct.setStatus(ProductStatusEnum.valueOf(request.getStatus().trim().toUpperCase()));
        existingProduct.setUpdatedAt(LocalDateTime.now());

        existingProduct.setCategory(entityFind.findCategory(request.getCategoryId()));
        existingProduct.setWarehouse(entityFind.findWarehouse(request.getWarehouseId()));
        existingProduct.setSupplier(entityFind.findSupplier(request.getSupplierId()));

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
            product.setStatus(ProductStatusEnum.valueOf(request.getStatus().trim().toUpperCase()));
            product.setCreatedAt(LocalDateTime.now());

            product.setCategory(entityFind.findCategory(request.getCategoryId()));
            product.setWarehouse(entityFind.findWarehouse(request.getWarehouseId()));
            product.setSupplier(entityFind.findSupplier(request.getSupplierId()));
        }

        return savedProducts;
    }

}
