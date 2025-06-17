package product.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import product.api.entity.Supplier;
import product.api.response.Response;
import product.api.service.SupplierService;
import product.api.utils.ResponseUtil;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getSupplierById(@PathVariable Long id) {
        supplierService.findSupplier(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, id);
    }

    @PostMapping
    public ResponseEntity<Response> createSupplier(@RequestBody Supplier supplier) {
        return ResponseUtil.buildResponse(HttpStatus.CREATED,supplierService.createSupplier(supplier));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateSupplier(@PathVariable Long id, @RequestBody Supplier updatedSupplier) {
        return ResponseUtil.buildResponse(HttpStatus.OK,supplierService.updateSupplier(id,updatedSupplier));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, id);
    }
}

