package product.api.service;

import org.springframework.stereotype.Service;
import product.api.entity.StockAdjustment;
import product.api.repository.StockAdjustmentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StockAdjustmentService {

    private final StockAdjustmentRepository stockAdjustmentRepository;

    public StockAdjustmentService(StockAdjustmentRepository stockAdjustmentRepository) {
        this.stockAdjustmentRepository = stockAdjustmentRepository;
    }

    public Optional<StockAdjustment> getStockAdjustmentById(Long id) {
        return stockAdjustmentRepository.findById(id);
    }

    public List<StockAdjustment> getAllStockAdjustments() {
        return stockAdjustmentRepository.findAll();
    }

    public StockAdjustment createStockAdjustment(StockAdjustment stockAdjustment) {
        return stockAdjustmentRepository.save(stockAdjustment);
    }

    public void deleteStockAdjustment(Long id) {
        stockAdjustmentRepository.deleteById(id);
    }
}
