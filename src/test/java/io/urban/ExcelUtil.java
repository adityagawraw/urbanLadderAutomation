package io.urban;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelUtil {
    public void enterProductData(List<Product> products) throws IOException {
        String filePath = "/home/aditya/Documents/file2.xlsx";

        XSSFWorkbook workbook  = new XSSFWorkbook(new FileInputStream(filePath));
        XSSFSheet sheet = workbook.getSheet("Sheet1");
            int rowsFilled = sheet.getPhysicalNumberOfRows();

        System.out.println(products.size());
        for(int i=1;i<products.size();i++){
            Row row = sheet.createRow(i+1);

            Cell productName = row.createCell(0);
            productName.setCellValue(products.get(i-1).getProductName());
            Cell finalPrice = row.createCell(1);
            finalPrice.setCellValue(products.get(i-1).getFinalPrice());
        }
            FileOutputStream fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);
            fileOut.close();
            System.out.println("Data saved to Excel file successfully.");

    }
}
