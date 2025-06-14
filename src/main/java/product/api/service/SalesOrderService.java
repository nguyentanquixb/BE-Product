package product.api.service;

import org.springframework.stereotype.Service;
import product.api.entity.SalesOrder;
import product.api.repository.SalesOrderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;

    public SalesOrderService(SalesOrderRepository salesOrderRepository) {
        this.salesOrderRepository = salesOrderRepository;
    }

    public Optional<SalesOrder> getSalesOrderById(Long id) {
        return salesOrderRepository.findById(id);
    }

    public List<SalesOrder> getAllSalesOrders() {
        return salesOrderRepository.findAll();
    }

    public SalesOrder createSalesOrder(SalesOrder salesOrder) {
        return salesOrderRepository.save(salesOrder);
    }

    public void deleteSalesOrder(Long id) {
        salesOrderRepository.deleteById(id);
    }
}
