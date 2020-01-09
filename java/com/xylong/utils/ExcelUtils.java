package com.welab.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

import com.welab.bean.UseCase;
import com.welab.dataprovider.TestDataProvider;
 
public class ExcelUtils {
	
    private static XSSFSheet ExcelWSheet;
    private static XSSFWorkbook ExcelWBook;
    private static XSSFCell Cell;
    
    public static Object[][] getTableArray(String FilePath,String SheetName ,String methodName) throws Exception {   
    	Object[][] tabArray = null;
    	try {
           FileInputStream ExcelFile = new FileInputStream(FilePath);
           // Access the required test data sheet
           ExcelWBook = new XSSFWorkbook(ExcelFile);
           ExcelWSheet = ExcelWBook.getSheet(SheetName);
           List<UseCase> list =new ArrayList<UseCase>();
           
           int totalRows = ExcelWSheet.getLastRowNum();
           int startNum = 0;
           String cellValue = "";
           for (int i = 0; i <= totalRows; i++) {
        	   cellValue = getCellData(i,0);
        	   if(cellValue==null && "".equals(cellValue)){
        		   continue;
        	   }else if(cellValue.equals(methodName)){
        		   startNum = i+2;
        		   break;
        	   }
           }
           if(startNum != 0){
        	   for (int j = startNum; j <= totalRows; j++) {
            	   if(getCellData(j,0) == null || "".equals(getCellData(j,0))){
            		   break;
            	   }else if(!"Y".equals(getCellData(j,7))){
        			   continue;
            	   }else{
            		   UseCase  useCase = new UseCase();
        			   useCase.setParam(getCellData(j,0));
        			   useCase.setVerify(getCellData(j,1));
        			   useCase.setDescribe(getCellData(j,2));
        			   useCase.setUrl(getCellData(j,3));
        			   useCase.setMethod(getCellData(j,4));
        			   useCase.setSave(getCellData(j,5));
        			   useCase.setPreParam(getCellData(j,6));
        			   useCase.setRun(getCellData(j,7));
        			   useCase.setSleepTime(getCellData(j,8));
            		   list.add(useCase);
            	   }
               }
        	   tabArray = new Object[list.size()][1];
               for (int i = 0; i < list.size(); i++) {
            	   tabArray[i][0] = list.get(i);
               }
           }else{
        	   tabArray[0][0] = null;
           }
        }
    	catch (FileNotFoundException e){
            System.out.println("Could not read the Excel sheet");
            e.printStackTrace();
        }
        catch (IOException e){
            System.out.println("Could not read the Excel sheet");
            e.printStackTrace();
        }
        return(tabArray);
     }
    
    public static String getCellData(int RowNum, int ColNum) throws Exception {
        try{
        	try {
        		Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
			} catch (NullPointerException e) {
				Cell = null;
			}
        	String CellData = "";
            if(Cell != null){
            	int dataType = Cell.getCellType();
            	if  (dataType == 3) {
                    return "";
                }
                else{
                	Cell.setCellType(Cell.CELL_TYPE_STRING);
                    CellData = Cell.getStringCellValue();
                }
            }
            return CellData;
        }catch (Exception e){
            //System.out.println(e.getMessage());
            throw (e);
        }
    }
    
}
	
	