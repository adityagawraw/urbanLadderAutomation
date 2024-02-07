package io.urban;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ExcelUtil {
    public void enterProductData(List<Product> products)  {
        String fileName = new Date().toString()+".xlsx";
        String filePath = "/home/aditya/Documents/"+new Date().toString()+".xlsx";

        try{
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Sheet1");

            Row rowHeader = sheet.createRow( 0);
            rowHeader.createCell(0).setCellValue("Serial No.");
            rowHeader.createCell(1).setCellValue("Product Name");
            rowHeader.createCell(2).setCellValue("Final Price");

            for (int i = 1; i < products.size(); i++) {
                Row row = sheet.createRow( i);

                Cell serialName = row.createCell(0);
                serialName.setCellValue( i);

                Cell productName = row.createCell(1);
                productName.setCellValue(products.get(i - 1).getProductName());

                Cell finalPrice = row.createCell(2);
                finalPrice.setCellValue(products.get(i - 1).getFinalPrice());
            }

            FileOutputStream fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);
            fileOut.close();
            System.out.println("Products data saved to " + fileName+ "Excel file successfully.");
        }
        catch (IOException e){
            System.out.println(e);
        }
    }
}
