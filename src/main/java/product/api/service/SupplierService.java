package product.api.service;

import org.springframework.stereotype.Service;
import product.api.entity.Supplier;
import product.api.exception.NotFoundException;
import product.api.repository.SupplierRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Supplier findById(Long id) {
        return supplierRepository.findById(id).orElseThrow(() -> new NotFoundException("supplier"));
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Long id, Supplier updatedSupplier) {
        Supplier supplier = findById(id);
        supplier.setName(updatedSupplier.getName());
        supplier.setContactInfo(updatedSupplier.getContactInfo());
        supplier.setAddress(updatedSupplier.getAddress());
        return supplierRepository.save(supplier);
    }

    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }

    public Supplier findSupplier(Long id) {
        return supplierRepository.findById(id).orElseThrow(() -> new NotFoundException("Supplier"));
    }
}
