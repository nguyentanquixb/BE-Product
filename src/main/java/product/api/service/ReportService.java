package product.api.service;

import org.springframework.stereotype.Service;
import product.api.dto.InventoryReportDTO;
import product.api.dto.TransactionReportDTO;
import product.api.entity.InventoryTransaction;
import product.api.entity.Product;
import product.api.repository.InventoryTransactionRepository;
import product.api.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ProductRepository productRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;

    public ReportService(ProductRepository productRepository, InventoryTransactionRepository inventoryTransactionRepository) {
        this.productRepository = productRepository;
        this.inventoryTransactionRepository = inventoryTransactionRepository;
    }

    public List<InventoryReportDTO> getInventoryReport() {
        List<Product> products = productRepository.findAll();
        List<InventoryReportDTO> reportList = new ArrayList<>();

        for (Product product : products) {
            BigDecimal totalValue = BigDecimal.valueOf(product.getQuantity()).multiply(product.getPrice());
            double totalValueAsDouble = totalValue.doubleValue();

            InventoryReportDTO report = InventoryReportDTO.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .quantity(product.getQuantity())
                    .totalValue(totalValueAsDouble)
                    .build();

            reportList.add(report);
        }

        return reportList;
    }

    public List<TransactionReportDTO> getTransactionReport(LocalDate dateFrom, LocalDate dateTo) {
        List<InventoryTransaction> transactions = inventoryTransactionRepository.findByTransactionDateBetween(
                dateFrom.atStartOfDay(), dateTo.atTime(23, 59, 59)
        );

        List<TransactionReportDTO> reportList = new ArrayList<>();

        for (InventoryTransaction transaction : transactions) {
            TransactionReportDTO report = TransactionReportDTO.builder()
                    .transactionId(transaction.getId())
                    .product(transaction.getProduct())
                    .transactionType(transaction.getType())
                    .quantity(transaction.getQuantity())
                    .transactionDate(transaction.getTransactionDate())
                    .build();

            reportList.add(report);
        }
        return reportList;
    }

}

