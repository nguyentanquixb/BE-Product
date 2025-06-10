package product.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import product.api.dto.ProductRequest;
import product.api.entity.Product;
import product.api.response.ProductResponse;
import product.api.response.Response;
import product.api.service.ProductService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getProductById(@PathVariable("id") Long id){
        Optional<Product> optionalProduct =productService.getProductById(id);
        if(optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            ProductResponse productResponse = ProductResponse.convertProduct(product);
            return ResponseEntity.ok(Response.ok(productResponse));
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.error("Product Not Found"));
        }

    }

//    @GetMapping()
//    public ResponseEntity<Response> getAllProduct(){
//        List<Product> products =productService.getAllProduct();
//        List<ProductResponse> productResponse = products.stream().map(ProductResponse::convertProduct).toList();
//        return ResponseEntity.ok(Response.ok(productResponse));
//    }
    @PostMapping
    public ResponseEntity<Response> createProduct(@RequestBody ProductRequest request) {

        if(request.getName() == null || request.getName().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Name is null or empty "));
        }
        if(request.getProductCode() == null || request.getProductCode().isEmpty() || request.getProductCode().length() > 50){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Product Code is null or empty or no more char 50 "));
        }
        if(request.getDescription().length() > 1000){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Description is null or no more char 1000 "));
        }
        if(request.getPrice() == null || request.getPrice().doubleValue() < 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Price is null or negative "));
        }
        if(request.getQuantity() == null || request.getQuantity() < 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Quantity is null or negative "));
        }
        List<String> validStatus = Arrays.asList("active", "inactive");

        String status = request.getStatus() == null ? "active" : request.getStatus().trim().toLowerCase();

        if (!validStatus.contains(status)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Response.error("Status is invalid"));
        }
        if (request.getCreatedAt() == null || request.getCreatedAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Response.error("CreatedAt is invalid"));
        }
        if (request.getProductCode() == null || request.getProductCode().isEmpty() || request.getProductCode().length() > 50 || productService.isProductCodeDuplicate(request.getProductCode())) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Response.error("Product Code is invalid or no more char 50 or product already exists"));
        }


        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setUnit(request.getUnit());
        product.setStatus(request.getStatus());
        product.setProductCode(request.getProductCode());
        product.setCreatedAt(LocalDateTime.now());

        Product saveProduct = productService.createProduct(product);
        ProductResponse productResponse = ProductResponse.convertProduct(saveProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.ok(productResponse));


    }
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {

        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.error("Product not found"));

        if(request.getName() == null || request.getName().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Name is null or empty"));
        }
        if(request.getProductCode() == null || request.getProductCode().isEmpty() || request.getProductCode().length() > 50 || productService.isProductCodeDuplicate(request.getProductCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Product Code is invalid or  no more char 50 or product already exists"));
        }
        if(request.getDescription().length() > 1000){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Description exceeds 1000 characters"));
        }
        if(request.getPrice() == null || request.getPrice().doubleValue() < 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Price is invalid"));
        }
        if(request.getQuantity() == null || request.getQuantity() < 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Quantity is invalid"));
        }

        List<String> validStatusUpdate = Arrays.asList("active", "inactive");
        String status = request.getStatus() == null ? "active" : request.getStatus().trim().toLowerCase();

        if (!validStatusUpdate.contains(status)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Status is invalid"));
        }

        Product product = productOptional.get();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setUnit(request.getUnit());
        product.setStatus(status);
        product.setProductCode(request.getProductCode());
        product.setUpdatedAt(LocalDateTime.now());

        Product updatedProduct = productService.updateProduct(product);
        ProductResponse productResponse = ProductResponse.convertProduct(updatedProduct);

        return ResponseEntity.status(HttpStatus.OK).body(Response.ok(productResponse));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteProduct(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);

        if (product.isPresent()) {
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK).body(Response.ok("Product deleted successfully"));

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.error("Product not found"));
        }
    }
    @GetMapping
    public ResponseEntity<Response> getAllProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam( defaultValue = "10") int size
    ) {
        Page<Product> products = productService.getProductByPage(PageRequest.of(page, size));
        List<ProductResponse> productResponses = products.getContent().stream().map(ProductResponse::convertProduct).toList();
        return ResponseEntity.ok(Response.ok(productResponses));
    }
    @GetMapping("/search")
    public ResponseEntity<Response> searchProduct(@RequestParam String keyword){
        List<Product> products = productService.searchProductByName(keyword);
        List<ProductResponse> productResponses = products.stream().map(ProductResponse::convertProduct).toList();
        return ResponseEntity.ok(Response.ok(productResponses));
    }

    @DeleteMapping("/delete-list")
    public ResponseEntity<Response> deleteProducts(@RequestBody List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Product list is empty"));
        }

        List<Long> notIds = new ArrayList<>();

        for (Long id : productIds) {
            Optional<Product> product = productService.getProductById(id);

            if (product.isPresent()) {
                productService.deleteProduct(id);
            } else {
                notIds.add(id);
            }
        }

        if (notIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(Response.ok("All product deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(Response.error("Some products not found"));
        }
    }

    @PutMapping("/update-list")
    public ResponseEntity<Response> updateProducts(@RequestBody List<ProductRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Product list is empty"));
        }
        Set<String> processedProductCodes = new HashSet<>();
        List<ProductResponse> updatedProducts = new ArrayList<>();

        for (ProductRequest request : requests) {
            if (processedProductCodes.contains(request.getProductCode())) {
                continue;
            }

            Optional<Product> productOptional = productService.getProductById(request.getId());

            if (productOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.error("products not found"));
            }

            if (request.getName() == null || request.getName().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Name is null or empty"));
            }
            if (request.getProductCode() == null || request.getProductCode().isEmpty() || request.getProductCode().length() > 50 || productService.isProductCodeDuplicate(request.getProductCode())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Product Code is invalid or exceeds 50 characters or product already exists"));
            }
            if (request.getDescription() != null && request.getDescription().length() > 1000) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Description exceeds 1000 characters"));
            }
            if (request.getPrice() == null || request.getPrice().doubleValue() < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Price is invalid"));
            }
            if (request.getQuantity() == null || request.getQuantity() < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Quantity is invalid"));
            }

            List<String> validStatusUpdate = Arrays.asList("active", "inactive");
            String status = request.getStatus() == null ? "active" : request.getStatus().trim().toLowerCase();

            if (!validStatusUpdate.contains(status)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Status is invalid"));
            }

            Product product = productOptional.get();
            product.setName(request.getName());
            product.setDescription(request.getDescription());
            product.setPrice(request.getPrice());
            product.setQuantity(request.getQuantity());
            product.setUnit(request.getUnit());
            product.setStatus(status);
            product.setProductCode(request.getProductCode());
            product.setUpdatedAt(LocalDateTime.now());

            Product updatedProduct = productService.updateProduct(product);
            updatedProducts.add(ProductResponse.convertProduct(updatedProduct));

            processedProductCodes.add(request.getProductCode());
        }

        return ResponseEntity.status(HttpStatus.OK).body(Response.ok(updatedProducts));
    }
}
