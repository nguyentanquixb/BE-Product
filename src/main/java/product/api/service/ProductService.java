package product.api.service;

import org.springframework.stereotype.Service;
import product.api.entity.Product;
import product.api.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> getProductById(Long id){
        return productRepository.findById(id);
    }

    public List<Product> getAllProduct(){
        List<Product> product =  productRepository.findAll();
        return product;
    }

    public Product createProduct(Product request) {
        return productRepository.save(request);
    }

    public Optional<Product> findProductByProductCode(String productCode){
        return productRepository.findByProductCode(productCode);
    }

    public boolean isProductCodeDuplicate(String productCode) {
        return productRepository.existsProductByProductCode(productCode);
    }

    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    public Product deleteProduct(Long id) {
        return productRepository.deleteProductById(id);
    }



}
