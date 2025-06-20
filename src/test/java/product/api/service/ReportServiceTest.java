package product.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import product.api.dto.InventoryReportDTO;
import product.api.entity.Product;
import product.api.repository.InventoryTransactionRepository;
import product.api.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InventoryTransactionRepository inventoryTransactionRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    public void getInventoryReport_SuccessTest() {

        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Test Product 1");
        product1.setQuantity(50);
        product1.setPrice(new BigDecimal("100.00"));

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Test Product 2");
        product2.setQuantity(30);
        product2.setPrice(new BigDecimal("200.00"));

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        when(productRepository.findAll()).thenReturn(products);

        List<InventoryReportDTO> result = reportService.getInventoryReport();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getProductId());
        assertEquals("Test Product 1", result.get(0).getProductName());
        assertEquals(50, result.get(0).getQuantity());
        assertEquals(5000.0, result.get(0).getTotalValue());
        assertEquals(2L, result.get(1).getProductId());
        assertEquals("Test Product 2", result.get(1).getProductName());
        assertEquals(30, result.get(1).getQuantity());
        assertEquals(6000.0, result.get(1).getTotalValue());
    }

}