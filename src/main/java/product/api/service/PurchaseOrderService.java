package product.api.service;

import org.springframework.stereotype.Service;
import product.api.dto.PurchaseOrderDetailRequest;
import product.api.dto.PurchaseOrderRequest;
import product.api.entity.OrderStatusEnum;
import product.api.entity.PurchaseOrder;
import product.api.repository.PurchaseOrderRepository;
import product.api.response.PurchaseOrderResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderService {
    
    private final PurchaseOrderRepository purchaseOrderRepository;
    
    private final SupplierService supplierService;
    

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository,SupplierService  supplierService) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.supplierService = supplierService;
    }

    public Optional<PurchaseOrder> getPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id);
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    public void deletePurchaseOrder(Long id) {
        purchaseOrderRepository.deleteById(id);
    }

    public PurchaseOrderResponse createPurchaseOrder(PurchaseOrderRequest request) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setSupplier(supplierService.findSupplier(request.getSupplierId()));
        purchaseOrder.setStatus(OrderStatusEnum.PENDING);
        purchaseOrder.setOrderDate(LocalDateTime.now());
        purchaseOrder.setCreatedAt(LocalDateTime.now());

        BigDecimal totalAmount = calculateTotalAmount(request.getDetails());
        purchaseOrder.setTotalAmount(totalAmount);

        PurchaseOrder savedOrder = purchaseOrderRepository.save(purchaseOrder);
        return PurchaseOrderResponse.convertPurchaseOrder(savedOrder);
    }

    public BigDecimal calculateTotalAmount(List<PurchaseOrderDetailRequest> details) {
        BigDecimal total = BigDecimal.ZERO;

        for (PurchaseOrderDetailRequest detail : details) {
            BigDecimal itemTotal = detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity()));
            total = total.add(itemTotal);
        }

        return total;
    }
}
