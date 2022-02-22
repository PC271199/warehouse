package com.example.Warehouse.util;

import java.io.File;
import java.nio.file.Files;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Warehouse.entities.bukkenService.Bukken;
import com.example.Warehouse.repositories.systemService.FileRepository;


public class BukkenExcelExporter {

	private static final String DATA_EXPORT_PATH = "./src/main/resources/file/dataExport.xlsx";
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<Bukken> listUsers;

	public BukkenExcelExporter(List<Bukken> listUsers) {
		this.listUsers = listUsers;
		workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("Users");

		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);

		createCell(row, 0, "Bukken ID", style);
		createCell(row, 1, "Name", style);
		createCell(row, 2, "Latitude", style);
		createCell(row, 3, "Longitude", style);
		createCell(row, 4, "Rent Fee", style);
		createCell(row, 5, "Area", style);

	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	private void writeDataLines() {
		int rowCount = 1;

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);

		for (Bukken user : listUsers) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;

			createCell(row, columnCount++, user.getId() + "", style);
			createCell(row, columnCount++, user.getName() + "", style);
			createCell(row, columnCount++, user.getLatitude() + "", style);
			createCell(row, columnCount++, user.getLongitude() + "", style);
			createCell(row, columnCount++, user.getRentFee()+"", style);
			createCell(row, columnCount++, user.getArea()+"", style);

		}
	}

	public com.example.Warehouse.entities.fileService.File export(HttpServletResponse response) throws IOException {
		writeHeaderLine();
		writeDataLines();

//		ServletOutputStream outputStream = response.getOutputStream();
//		workbook.write(outputStream);
		try {
			// Write the workbook in file system
			FileOutputStream out = new FileOutputStream(new File(DATA_EXPORT_PATH));
			byte[] content = null;
			

			workbook.write(out);
			workbook.close();
			out.close();
			try {
				Path path = Paths.get(DATA_EXPORT_PATH);
				byte[] data = Files.readAllBytes(path);
				com.example.Warehouse.entities.fileService.File myFile = new com.example.Warehouse.entities.fileService.File();
				myFile.setData(data);
				myFile.setName("dataExport.xlsx");
				myFile.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				return myFile;
			} catch (final IOException e) {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
}
