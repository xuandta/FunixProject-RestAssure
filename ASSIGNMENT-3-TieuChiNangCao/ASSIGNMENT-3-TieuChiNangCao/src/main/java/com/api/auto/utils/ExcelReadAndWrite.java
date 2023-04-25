package com.api.auto.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReadAndWrite {

	public static XSSFWorkbook ReadFile(String FilePath) throws IOException {
		FileInputStream file = new FileInputStream(FilePath);
		XSSFWorkbook  wb = new XSSFWorkbook(file);
		return wb;
	}
	
	public static String getValuefromKey(String ExcelPath, String SheetName, String key) throws IOException {
		FileInputStream file = new FileInputStream(ExcelPath);
		XSSFWorkbook  wb = new XSSFWorkbook(file);	
		XSSFSheet sheet = wb.getSheet(SheetName) ;
		int NumberRow = sheet.getLastRowNum();
		String value = "Không tìm thấy giá trị";
		for (int i =0; i<NumberRow+1; i++) { 
			if( sheet.getRow(i).getCell(0).getStringCellValue().equals(key)) {
				value =  sheet.getRow(i).getCell(1).getStringCellValue(); 
				return value;
			}
		}
		System.out.println("Không tìm thấy giá trị của: " + key);
		return value;
	}
	
	
	
	
	
	public static void SaveToken(String token) throws IOException {
		FileOutputStream file = new FileOutputStream("./data/token.xlsx");
		XSSFWorkbook  wb = new XSSFWorkbook();
		XSSFSheet s0 = wb.createSheet("Sheet1");
		XSSFRow  r0 = s0.createRow(0);
		XSSFCell  c0 = r0.createCell(0);
		c0.setCellValue(token);
		wb.write(file);
		wb.close();
	}
	
	public static String getToken() throws IOException {
		FileInputStream file = new FileInputStream("./data/token.xlsx");
		XSSFWorkbook  wb = new XSSFWorkbook(file);	
		XSSFSheet sheet = wb.getSheet("Sheet1") ;
		return sheet.getRow(0).getCell(0).getStringCellValue();
	}
	
}
