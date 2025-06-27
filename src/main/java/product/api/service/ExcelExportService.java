package product.api.service;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import product.api.response.InventoryTransactionResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class ExcelExportService {

    public void exportInventoryTransaction(List<InventoryTransactionResponse> transactionResponse, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Inventory Transactions");
        Row headerRow = sheet.createRow(0);
        String[] headers = { "ID", "Product Name", "Warehouse","Location", "Type", "Quantity", "Transaction Date"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        CreationHelper creationHelper = workbook.getCreationHelper();
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/MM/yyyy HH:mm"));

        int rowNum = 1;
        for (InventoryTransactionResponse transaction : transactionResponse) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(transaction.getId());
            row.createCell(1).setCellValue(transaction.getProduct().getName());
            row.createCell(2).setCellValue(transaction.getWarehouse().getName());
            row.createCell(3).setCellValue(transaction.getWarehouse().getLocation());
            row.createCell(4).setCellValue(String.valueOf(transaction.getType()));
            row.createCell(5).setCellValue(transaction.getProduct().getQuantity());
            Cell dateCell = row.createCell(6);
            if (transaction.getTransactionDate() != null) {
                Date date = Date.from(transaction.getTransactionDate().atZone(ZoneId.systemDefault()).toInstant());
                dateCell.setCellValue(date);
                dateCell.setCellStyle(dateCellStyle);
            }
        }

        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String filename = "inventory_transactions_" + currentDateTime + ".xlsx";

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);



        ServletOutputStream servletOutputStream = response.getOutputStream();
        workbook.write(servletOutputStream);
        workbook.close();
        servletOutputStream.close();

    }
}
