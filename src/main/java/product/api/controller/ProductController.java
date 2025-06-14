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
import product.api.response.ProductResponse;
import product.api.response.Response;
import product.api.service.ProductService;
import product.api.service.S3Service;
import product.api.utils.ExcelHelper;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;

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

    public ProductController(ProductService productService, S3Service s3Service) {
        this.productService = productService;
        this.s3Service = s3Service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getProductById(@PathVariable("id") Long id) {
        Optional<Product> optionalProduct = productService.getProductById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            System.out.println("Product Status from DB: " + product.getStatus());

            ProductResponse productResponse = ProductResponse.convertProduct(product);
            return ResponseEntity.ok(Response.ok(productResponse));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.error("Product Not Found"));
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

        // Kiểm tra dữ liệu đầu vào
        if (request.getName() == null || request.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Name is required"));
        }
        if (request.getProductCode() == null || request.getProductCode().isEmpty() || request.getProductCode().length() > 50) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Product Code is required and must be less than 50 characters"));
        }
        if (request.getDescription() != null && request.getDescription().length() > 1000) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Description must be less than 1000 characters"));
        }
        if (request.getPrice() == null || request.getPrice().doubleValue() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Price is required and must be positive"));
        }
        if (request.getQuantity() == null || request.getQuantity() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Quantity must be a positive integer"));
        }
        if (request.getMinStock() == null || request.getMinStock() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Min Stock must be a positive integer"));
        }
        if (request.getCategoryId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Category ID is required"));
        }
        if (request.getWarehouseId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Warehouse ID is required"));
        }
        if (request.getSupplierId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Supplier ID is required"));
        }

        // Xác định trạng thái sản phẩm
        ProductStatusEnum status;
        try {
            status = request.getStatus() == null ? ProductStatusEnum.ACTIVE : ProductStatusEnum.valueOf(request.getStatus().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Invalid status. Allowed values: ACTIVE, INACTIVE"));
        }

        // Kiểm tra ngày tạo
        if (request.getCreatedAt() == null || request.getCreatedAt().isAfter(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("CreatedAt is invalid"));
        }

        // Kiểm tra mã sản phẩm trùng lặp
        if (productService.isProductCodeDuplicate(request.getProductCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Product Code already exists"));
        }

        // Tạo sản phẩm mới
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setMinStock(request.getMinStock());
        product.setUnit(request.getUnit());
        product.setBarcode(request.getBarcode());
        product.setStatus(status);
        product.setProductCode(request.getProductCode());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        product.setCategory(categoryRepository.findById(request.getCategoryId()).orElseThrow(
                () -> new RuntimeException("Category not found")));
        product.setWarehouse(warehouseRepository.findById(request.getWarehouseId()).orElseThrow(
                () -> new RuntimeException("Warehouse not found")));
        product.setSupplier(supplierRepository.findById(request.getSupplierId()).orElseThrow(
                () -> new RuntimeException("Supplier not found")));

        // Lưu vào DB
        Product savedProduct = productService.createProduct(product);
        ProductResponse productResponse = ProductResponse.convertProduct(savedProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.ok(productResponse));
    }



    @PostMapping("/create-product-excel")
    @PreAuthorize("hasAuthority('CREATE_PRODUCT')")
    public ResponseEntity<Response> createProductExcel(@RequestParam("file") MultipartFile file) {

        logger.info("API /create-product-excel called at {}", LocalDateTime.now());

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
                ProductRequest request = productRequests.get(i);

                if (request.getName() == null || request.getName().isEmpty()) {
                    errors.add("Row " + (i + 1) + ": Product name is empty or invalid");
                }

                if (request.getProductCode() == null || request.getProductCode().isEmpty()) {
                    errors.add("Row " + (i + 1) + ": Product code is empty or invalid");
                } else if (request.getProductCode().length() > 50) {
                    errors.add("Row " + (i + 1) + ": Product code exceeds 50 characters");
                } else if (productService.isProductCodeDuplicate(request.getProductCode())) {
                    errors.add("Row " + (i + 1) + ": Product code '" + request.getProductCode() + "' already exists");
                }

                if (request.getPrice() == null || request.getPrice().doubleValue() < 0) {
                    errors.add("Row " + (i + 1) + ": Price is empty or negative");
                }

                if (request.getQuantity() == null || request.getQuantity() < 0) {
                    errors.add("Row " + (i + 1) + ": Quantity is empty or negative");
                }

                try {
                    ProductStatusEnum.valueOf(request.getStatus().trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    errors.add("Row " + (i + 1) + ": Invalid product status '" + request.getStatus() + "'");
                }
            }

            if (!errors.isEmpty()) {
                s3Service.uploadFile(tempFile, "error", "validation-failed-" + timestamp + "-" + fileName);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.error(errors));
            }

            List<ProductResponse> savedProducts = new ArrayList<>();
            for (ProductRequest request : productRequests) {
                Product product = new Product();
                product.setName(request.getName());
                product.setProductCode(request.getProductCode());
                product.setDescription(request.getDescription());
                product.setPrice(request.getPrice());
                product.setQuantity(request.getQuantity());
                product.setUnit(request.getUnit());
                product.setStatus(ProductStatusEnum.valueOf(request.getStatus().trim().toUpperCase()));
                product.setCreatedAt(LocalDateTime.now());

                Product savedProduct = productService.createProduct(product);
                savedProducts.add(ProductResponse.convertProduct(savedProduct));
            }

            s3Service.uploadFile(tempFile, "success", "success-" + timestamp + "-" + fileName);
            logger.info("Successfully processed and uploaded file to S3: success/{}", "success-" + timestamp + "-" + fileName);
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

        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.error("Product not found"));

        if (request.getName() == null || request.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Name is null or empty"));
        }
        if (request.getProductCode() == null || request.getProductCode().isEmpty() || request.getProductCode().length() > 50 || productService.isProductCodeDuplicate(request.getProductCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Product Code is invalid or  no more char 50 or product already exists"));
        }
        if (request.getDescription().length() > 1000) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Description exceeds 1000 characters"));
        }
        if (request.getPrice() == null || request.getPrice().doubleValue() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Price is invalid"));
        }
        if (request.getQuantity() == null || request.getQuantity() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Quantity is invalid"));
        }

        ProductStatusEnum status;
        try {
            status = request.getStatus() == null ? ProductStatusEnum.ACTIVE : ProductStatusEnum.valueOf(request.getStatus().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error("Invalid status. Allowed values: ACTIVE, INACTIVE"));
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

            ProductStatusEnum status;
            try {
                status = request.getStatus() == null ? ProductStatusEnum.ACTIVE : ProductStatusEnum.valueOf(request.getStatus().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
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
