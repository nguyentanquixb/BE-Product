package product.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import product.api.dto.ProductRequest;
import product.api.entity.Product;
import product.api.entity.ProductStatusEnum;
import product.api.repository.CategoryRepository;
import product.api.repository.SupplierRepository;
import product.api.repository.WarehouseRepository;
import product.api.response.ProductResponse;
import product.api.response.Response;
import product.api.service.ProductService;
import product.api.service.S3Service;
import product.api.utils.EntityFind;
import product.api.utils.ExcelHelper;
import product.api.utils.ResponseError;
import product.api.validate.ProductValidate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final S3Service s3Service;

    private final ProductService productService;

    private final CategoryRepository categoryRepository;

    private final WarehouseRepository warehouseRepository;

    private final SupplierRepository supplierRepository;

    @Autowired
    private ProductValidate productValidate;

    @Autowired
    private EntityFind entityFind;

    public ProductController(ProductService productService, S3Service s3Service, CategoryRepository categoryRepository, WarehouseRepository warehouseRepository, SupplierRepository supplierRepository, ProductValidate productValidator1) {
        this.productService = productService;
        this.s3Service = s3Service;
        this.categoryRepository = categoryRepository;
        this.warehouseRepository = warehouseRepository;
        this.supplierRepository = supplierRepository;

    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getProductById(@PathVariable("id") Long id) {
        Optional<Product> optionalProduct = productService.getProductById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            ProductResponse productResponse = ProductResponse.convertProduct(product);
            return ResponseEntity.ok(Response.ok(productResponse));
        } else {
            return ResponseError.errorResponse("Product Not Found");
        }

    }

    @GetMapping()
    public ResponseEntity<Response> getAllProduct() {
        List<Product> products = productService.getAllProduct();

        List<ProductResponse> productResponse = products.stream().map(ProductResponse::convertProduct).toList();
        return ResponseEntity.ok(Response.ok(productResponse));
    }

    @PostMapping("/create-product")
    @PreAuthorize("hasAuthority('CREATE_PRODUCT')")
    public ResponseEntity<Response> createProduct(@RequestBody ProductRequest request) {

       List<String> errors = productValidate.validateProduct(request);
       if(!errors.isEmpty()){
           return ResponseError.errorResponse(errors);
       }

       Product savedProduct = productService.createProduct(request);
       ProductResponse productResponse = ProductResponse.convertProduct(savedProduct);
       return ResponseEntity.status(HttpStatus.CREATED).body(Response.ok(productResponse));
    }

    @PostMapping("/create-product-excel")
    @PreAuthorize("hasAuthority('CREATE_PRODUCT')")
    public ResponseEntity<Response> createProductExcel(@RequestParam("file") MultipartFile file) {

        logger.info("API create-product-excel called at {}", LocalDateTime.now());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String fileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "products.xlsx";
        fileName = fileName.replaceAll("\\s+", "_");

        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("upload-", ".xlsx");
            file.transferTo(tempFile.toFile());

            if (file.isEmpty()) {
                s3Service.uploadFile(tempFile, "error", "empty-file-" + timestamp + "-" + fileName);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.error(List.of("File is empty or not provided")));
            }

            List<ProductRequest> productRequests;
            try {
                productRequests = ExcelHelper.excelToProductList(Files.newInputStream(tempFile));
            } catch (Exception e) {
                s3Service.uploadFile(tempFile, "error", "invalid-excel-" + timestamp + "-" + fileName);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.error(List.of("Error reading Excel file: " + e.getMessage())));
            }

            List<String> errors = new ArrayList<>();
            for (int i = 0; i < productRequests.size(); i++) {
                errors.addAll(productValidate.validateProductExcel(productRequests.get(i), i + 1));
            }

            if (!errors.isEmpty()) {
                s3Service.uploadFile(tempFile, "error", "validation-failed-" + timestamp + "-" + fileName);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.error(errors));
            }

            s3Service.uploadFile(tempFile, "success", "success-" + timestamp + "-" + fileName);
            logger.info("Successfully processed and uploaded file to S3: success/{}", "success-" + timestamp + "-" + fileName);
            List<ProductResponse> savedProducts = productService.createProducts(productRequests);
            return ResponseEntity.status(HttpStatus.CREATED).body(Response.ok(savedProducts));

        } catch (IOException e) {
            logger.error("Failed to upload file to S3: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error(List.of("Failed to upload file to S3: " + e.getMessage())));
        } finally {
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException e) {
                    logger.error("Failed to delete temp file: {}", e.getMessage());
                }
            }
        }
    }

    @PutMapping("/update-product/{id}")
    @PreAuthorize("hasAuthority('UPDATE_PRODUCT')")
    public ResponseEntity<Response> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {

        List<String> errors = productValidate.validateProduct(request);
        if(!errors.isEmpty()){
            return ResponseError.errorResponse(errors);
        }

        Product updatedProduct = productService.updateProduct(request);
        ProductResponse productResponse = ProductResponse.convertProduct(updatedProduct);

        return ResponseEntity.status(HttpStatus.OK).body(Response.ok(productResponse));
    }


    @DeleteMapping("delete-product/{id}")
    @PreAuthorize("hasAuthority('DELETE_PRODUCT')")
    public ResponseEntity<Response> deleteProduct(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);

        if (product.isPresent()) {
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK).body(Response.ok("Product deleted successfully"));

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.error("Product not found"));
        }
    }

    @GetMapping("/page")
    public ResponseEntity<Response> getAllProductPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Product> products = productService.getProductByPage(PageRequest.of(page, size));
        List<ProductResponse> productResponses = products.getContent().stream().map(ProductResponse::convertProduct).toList();
        return ResponseEntity.ok(Response.ok(productResponses));
    }

    @GetMapping("/search")
    public ResponseEntity<Response> searchProduct(@RequestParam String keyword) {
        List<Product> products = productService.searchProductByName(keyword);
        List<ProductResponse> productResponses = products.stream().map(ProductResponse::convertProduct).toList();
        return ResponseEntity.ok(Response.ok(productResponses));
    }

    @DeleteMapping("/delete-list")
    @PreAuthorize("hasAuthority('DELETE_PRODUCT')")
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
    @PreAuthorize("hasAuthority('UPDATE_PRODUCT')")
    public ResponseEntity<Response> updateProducts(@RequestBody List<ProductRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return ResponseError.errorResponse("Product list is empty");
        }

        Set<String> processedProductCodes = new HashSet<>();
        List<ProductResponse> updatedProducts = new ArrayList<>();

        for (ProductRequest request : requests) {
            if (processedProductCodes.contains(request.getProductCode())) {
                continue;
            }
            List<String> errors = productValidate.validateProduct(request);
            if (!errors.isEmpty()) {
                return ResponseError.errorResponse(errors);
            }

            Product updatedProduct = productService.updateProduct(request);
            updatedProducts.add(ProductResponse.convertProduct(updatedProduct));

            processedProductCodes.add(request.getProductCode());
        }

        return ResponseEntity.status(HttpStatus.OK).body(Response.ok(updatedProducts));
    }

}
