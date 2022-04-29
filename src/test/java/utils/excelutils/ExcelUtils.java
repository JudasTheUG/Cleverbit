package utils.excelutils;//reading value of a particular cell
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.saf.framework.MyTestNGBaseClass;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel. * ;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class ExcelUtils extends MyTestNGBaseClass {

    //method defined for reading a cell
    public String ReadCellData(int vRow, int vColumn) {
        String value = null; //variable for storing the cell value
        Workbook wbook = null; //initialize Workbook null
        try {
            //reading data from a file in the form of bytes
            FileInputStream fis = new FileInputStream("src/test/java/resources/Müşteri Numaraları.xlsx");
            //creates an XSSFWorkbook object by buffering the whole stream into the memory
            wbook = new XSSFWorkbook(fis);
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException e1) {
            e1.printStackTrace();
        }
        Sheet sheet = wbook.getSheetAt(0);
        //getting the XSSFSheet object at given index
        Row row = sheet.getRow(vRow);
        //returns the logical row
        Cell cell = row.getCell(vColumn);
        //getting the cell representing the given column
        value = String.valueOf(cell.getNumericCellValue());

        DataFormatter formatter = new DataFormatter();
        value = formatter.formatCellValue(cell);
        return value;

        //String value2=value.replace(".0","");
        //return value2;


        //DataFormatter formatter = new DataFormatter();
       //value = formatter.formatCellValue(cell);

        /*        value = String.valueOf(cell.getNumericCellValue());
        if(value.contains(".0")){
            value.s
        }
        //getting cell value
        return value;
        //returns the cell value*/


    }
    public String ReadCellDataString(int vRow, int vColumn) {
        String value = null; //variable for storing the cell value
        Workbook wbook = null; //initialize Workbook null
        try {
            //reading data from a file in the form of bytes
            FileInputStream fis = new FileInputStream("src/test/java/resources/Müşteri Numaraları.xlsx");
            //creates an XSSFWorkbook object by buffering the whole stream into the memory
            wbook = new XSSFWorkbook(fis);
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException e1) {
            e1.printStackTrace();
        }
        Sheet sheet = wbook.getSheetAt(0);
        //getting the XSSFSheet object at given index
        Row row = sheet.getRow(vRow);
        //returns the logical row
        Cell cell = row.getCell(vColumn);
        //getting the cell representing the given column
        value = cell.getStringCellValue();
        //getting cell value
        return value;
        //returns the cell value
    }

}