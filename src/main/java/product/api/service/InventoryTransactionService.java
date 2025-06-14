package product.api.service;

import org.springframework.stereotype.Service;
import product.api.entity.InventoryTransaction;
import product.api.repository.InventoryTransactionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryTransactionService {

    private final InventoryTransactionRepository inventoryTransactionRepository;

    public InventoryTransactionService(InventoryTransactionRepository inventoryTransactionRepository) {
        this.inventoryTransactionRepository = inventoryTransactionRepository;
    }

    public Optional<InventoryTransaction> getTransactionById(Long id) {
        return inventoryTransactionRepository.findById(id);
    }

    public List<InventoryTransaction> getAllTransactions() {
        return inventoryTransactionRepository.findAll();
    }

    public InventoryTransaction createTransaction(InventoryTransaction transaction) {
        return inventoryTransactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        inventoryTransactionRepository.deleteById(id);
    }
}
