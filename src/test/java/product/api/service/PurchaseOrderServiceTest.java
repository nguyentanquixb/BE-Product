package product.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import product.api.dto.PurchaseOrderDetailRequest;
import product.api.dto.PurchaseOrderRequest;
import product.api.entity.OrderStatusEnum;
import product.api.entity.PurchaseOrder;
import product.api.entity.Supplier;
import product.api.repository.PurchaseOrderRepository;
import product.api.response.PurchaseOrderResponse;

@ExtendWith(MockitoExtension.class)
public class PurchaseOrderServiceTest {

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;

    @Mock
    private SupplierService supplierService;

    @InjectMocks
    private PurchaseOrderService purchaseOrderService;

    @Test
    public void createPurchaseOrder_SuccessTest() {

        PurchaseOrderDetailRequest detail1 = new PurchaseOrderDetailRequest();
        detail1.setUnitPrice(new BigDecimal("100.00"));
        detail1.setQuantity(5);

        PurchaseOrderDetailRequest detail2 = new PurchaseOrderDetailRequest();
        detail2.setUnitPrice(new BigDecimal("200.00"));
        detail2.setQuantity(3);

        List<PurchaseOrderDetailRequest> details = Arrays.asList(detail1, detail2);

        PurchaseOrderRequest request = new PurchaseOrderRequest();
        request.setSupplierId(1L);
        request.setDetails(details);

        Supplier supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("Test Supplier");

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(1L);
        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setStatus(OrderStatusEnum.PENDING);
        purchaseOrder.setTotalAmount(new BigDecimal("1100.00"));

        when(supplierService.findSupplier(1L)).thenReturn(supplier);
        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(purchaseOrder);

        PurchaseOrderResponse response = purchaseOrderService.createPurchaseOrder(request);

        assertEquals(1L, response.getId());
        assertEquals("Test Supplier", response.getSupplierName());
        assertEquals(OrderStatusEnum.PENDING, response.getStatus());
        assertEquals(new BigDecimal("1100.00"), response.getTotalAmount());
    }

    @Test
    public void calculateTotalAmount_SuccessTest() {

        PurchaseOrderDetailRequest detail1 = new PurchaseOrderDetailRequest();
        detail1.setUnitPrice(new BigDecimal("100.00"));
        detail1.setQuantity(5);

        PurchaseOrderDetailRequest detail2 = new PurchaseOrderDetailRequest();
        detail2.setUnitPrice(new BigDecimal("200.00"));
        detail2.setQuantity(3);

        List<PurchaseOrderDetailRequest> details = Arrays.asList(detail1, detail2);

        BigDecimal totalAmount = purchaseOrderService.calculateTotalAmount(details);

        assertEquals(new BigDecimal("1100.00"), totalAmount);
    }
}