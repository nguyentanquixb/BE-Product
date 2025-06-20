package product.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import product.api.entity.Supplier;
import product.api.response.Response;
import product.api.service.SupplierService;
import product.api.utils.ResponseUtil;

import java.util.List;

@RestController
@RequestMapping("supplier")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_SUPPLIER')")
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_SUPPLIER')")
    public ResponseEntity<Response> getSupplierById(@PathVariable Long id) {
        supplierService.findSupplier(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_SUPPLIER')")
    public ResponseEntity<Response> createSupplier(@RequestBody Supplier supplier) {
        return ResponseUtil.buildResponse(HttpStatus.CREATED,supplierService.createSupplier(supplier));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_SUPPLIER')")
    public ResponseEntity<Response> updateSupplier(@PathVariable Long id, @RequestBody Supplier updatedSupplier) {
        return ResponseUtil.buildResponse(HttpStatus.OK,supplierService.updateSupplier(id,updatedSupplier));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_SUPPLIER')")
    public ResponseEntity<Response> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, id);
    }
}

