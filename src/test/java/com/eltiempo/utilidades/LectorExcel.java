package com.eltiempo.utilidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.util.NumberToTextConverter;




/**
 * @since 27/11/2017
 * @author bgaona
 *
 */
public class LectorExcel {

	  public List<Map<String, String>> getData(String excelFilePath, String sheetName){
		  try {
		        Sheet sheet = getSheetByName(excelFilePath, sheetName);
		        return readSheet(sheet);
		  }catch(Exception e) {
			  return readSheet(null);
		  }
	    }
		/**
		 * Obtiene los datos de un archivo de excel, teniendo en cuenta el nombre de la
		 * hoja
		 * 
		 * @since 27/11/2017
		 * @author bgaona
		 * @param excelFilePath
		 *            Ruta del libro de excel
		 * @param sheetName
		 *            Nombre de la hoja que contiene los datos
		 * @return
		 * @throws InvalidFormatException
		 *             Manejo de error por formato inválido
		 * @throws IOException
		 *             Manejo de error para el proceso de entrada y salida de datos
		 */
	    public List<Map<String, String>> getData(String excelFilePath, int sheetNumber)
	            throws InvalidFormatException, IOException {
	        Sheet sheet = getSheetByIndex(excelFilePath, sheetNumber);
	        return readSheet(sheet);
	    }

	    private Sheet getSheetByName(String excelFilePath, String sheetName) {
	    	try {  		
		        Sheet sheet =null;
		        	if(getWorkBook(excelFilePath).getSheet(sheetName)!=null) {
		        		sheet = getWorkBook(excelFilePath).getSheet(sheetName);
		        	}
		        return sheet;
		        
	    	}catch(Exception e) {
	    		 return null;
	    	}
	    }

	    private Sheet getSheetByIndex(String excelFilePath, int sheetNumber) throws IOException, InvalidFormatException {
	        Sheet sheet = getWorkBook(excelFilePath).getSheetAt(sheetNumber);
	        return sheet;
	    }

	    private Workbook getWorkBook(String excelFilePath) throws IOException, InvalidFormatException {
	        File fp = new File(excelFilePath);
	    	FileInputStream fpis = new FileInputStream(fp);
	    	return WorkbookFactory.create(fpis);
	    }

	    private List<Map<String, String>> readSheet(Sheet sheet) {
	        Row row;
	        int totalRow = sheet.getPhysicalNumberOfRows();
	        List<Map<String, String>> excelRows = new ArrayList<>();
	        int headerRowNumber = getHeaderRowNumber(sheet);
	        if (headerRowNumber != -1) {
	            int totalColumn = sheet.getRow(headerRowNumber).getLastCellNum();
	            int setCurrentRow = 1;
	            for (int currentRow = setCurrentRow; currentRow <= totalRow; currentRow++) {
	                row = getRow(sheet, sheet.getFirstRowNum() + currentRow);
	                LinkedHashMap<String, String> columnMapdata = new LinkedHashMap<>();
	                for (int currentColumn = 0; currentColumn < totalColumn; currentColumn++) {
	                    columnMapdata.putAll(getCellValue(sheet, row, currentColumn));
	                }
	                excelRows.add(columnMapdata);
	            }
	        }
	        return excelRows;
	    }

		private int getHeaderRowNumber(Sheet sheet) {
			Row row;
			int totalRow = sheet.getLastRowNum();
			for (int currentRow = 0; currentRow <= totalRow + 1; currentRow++) {
				row = getRow(sheet, currentRow);
				if (row != null) {
						row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
						return row.getRowNum();
				}
			}
			return (-1);
	    }

	    private Row getRow(Sheet sheet, int rowNumber) {
	        return sheet.getRow(rowNumber);
	    }

		private LinkedHashMap<String, String> getCellValue(Sheet sheet, Row row, int currentColumn) {
			
	        LinkedHashMap<String, String> columnMapdata = new LinkedHashMap<>();
	        Cell cell;
	        if (row == null) {
	        	if (sheet.getRow(sheet.getFirstRowNum()).getCell(currentColumn, MissingCellPolicy.CREATE_NULL_AS_BLANK)
	                    .getCellType() != CellType.BLANK) {
	                String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(currentColumn)
	                        .getStringCellValue();
	                columnMapdata.put(columnHeaderName, "");
	                
	        	}
	        } else {
	        	
	            cell = row.getCell(currentColumn, MissingCellPolicy.CREATE_NULL_AS_BLANK);
	                if (cell.getCellType() == CellType.STRING && sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK)
	                        .getCellType() != CellType.BLANK) {
	                    String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex())
	                            .getStringCellValue();
	                    columnMapdata.put(columnHeaderName, cell.getStringCellValue());
	                    
	            } else if (cell.getCellType() == CellType.NUMERIC && sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK)
	                        .getCellType() != CellType.BLANK) {
	                    String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex())
	                            .getStringCellValue();
	                    columnMapdata.put(columnHeaderName, NumberToTextConverter.toText(cell.getNumericCellValue()));

	            } else if (cell.getCellType() == CellType.BLANK) {
	                if (sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK)
	                        .getCellType() != CellType.BLANK) {
	                    String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex())
	                            .getStringCellValue();
	                    columnMapdata.put(columnHeaderName, "");
	                }
	            } else  if (cell.getCellType() == CellType.BOOLEAN && sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK)
	                        .getCellType() != CellType.BLANK) {
	                    String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex())
	                            .getStringCellValue();
	                    columnMapdata.put(columnHeaderName, Boolean.toString(cell.getBooleanCellValue()));
	                    
	            }else  if (cell.getCellType() == CellType.ERROR && sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex(), MissingCellPolicy.CREATE_NULL_AS_BLANK)
	                        .getCellType() != CellType.BLANK) {
	                    String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex())
	                            .getStringCellValue();
	                    columnMapdata.put(columnHeaderName, Byte.toString(cell.getErrorCellValue()));
	                
	            }
	        }
	        return columnMapdata;
	    }
	
	
}
