package product.api.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import product.api.dto.ProductRequest;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelHelper {

    public static List<ProductRequest> excelToProductList(InputStream inputStream) throws Exception {
        List<ProductRequest> productRequests = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;

            ProductRequest product = new ProductRequest();
            product.setName(row.getCell(0).getStringCellValue());
            product.setProductCode(row.getCell(1).getStringCellValue());
            product.setDescription(row.getCell(2).getStringCellValue());
            product.setPrice(BigDecimal.valueOf(row.getCell(3).getNumericCellValue()));
            product.setQuantity((int) row.getCell(4).getNumericCellValue());
            product.setUnit(row.getCell(5).getStringCellValue());
            product.setStatus(row.getCell(6).getStringCellValue());
            product.setCreatedAt(LocalDateTime.now());
            product.setBarcode(row.getCell(7).getStringCellValue());
            product.setMinStock((int) row.getCell(8).getNumericCellValue());
            product.setCategoryId((long) row.getCell(9).getNumericCellValue());
            product.setWarehouseId((long) row.getCell(10).getNumericCellValue());
            product.setSupplierId((long) row.getCell(11).getNumericCellValue());

            productRequests.add(product);
        }
        workbook.close();
        return productRequests;
    }
}
