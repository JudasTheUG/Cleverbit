package com.saf.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.saf.framework.HashMapNew;


public class DataDriver {
	
		//Variables
		String dataSheet;
		HashMap <String, String> orgDictionary = new HashMap<String, String>();
		HashMapNew Dictionary = new HashMapNew();
		HashMap <String, String> Environment = new HashMap<String, String>();
		public static ArrayList<Object> arlst = new ArrayList<Object>();
		public static ArrayList<Object> catoList = new ArrayList<Object>();
		public static ArrayList<String> ToWriteList = new ArrayList<String>();
		static HashMap <String,HashMap <String,String>> MultipleDictionary = new HashMap <String,HashMap <String,String>>();
		HashMapNew KeysToWrite = new HashMapNew();
		HashMapNew DBInfo = new HashMapNew();
		static DataDriver obj = new DataDriver();
		Sheet Sheet = null;
	
	public static HashMap getData(String calendarName, String TC, String TN) {
		obj.fGetData(AutomationConstants.testDataPath + calendarName , TC, TN);	
		return MultipleDictionary;
	}
			
	public HashMap fGetData(String calendarFile, String actionName, String testName){
		
		fGetDataForTest(calendarFile, actionName, testName);
		MultipleDictionary.put("Dictionary", Dictionary);
		MultipleDictionary.put("KeysToWrite", KeysToWrite);
		return MultipleDictionary;
	}
	
//	public int fGetRowNumber(Sheet sheet, int colIndex, String val) {
//		int iRowCount = Sheet.getLastRowNum();
//		
//		int iRow;
//		for(iRow=0;iRow<iRowCount;iRow++)
//		{
//			//Get row with test name and exist
//			if(sheet.getRow(iRow).getCell(colIndex).getStringCellValue().equalsIgnoreCase(val))
//				break;
//		}
//		
//		return iRow;
//	}
	
	//Gets the corresponding DB details from the details sheet based on the DBName it receives
	public HashMapNew fGetDBDetails(String dbName) {
		HashMapNew dbDetails = new HashMapNew();
		final String pathFile = AutomationConstants.testDataPath + AutomationConstants.CommonFileName;
		final String DBSheet = "DB_Details";
		final String PKColumn = "DB_NAME";
		
		int iColPK = fGetColumnIndex(pathFile, DBSheet, PKColumn);
		
		int iRowCnt = Sheet.getLastRowNum();
		
		int iRow;
		for(iRow=0;iRow<=iRowCnt;iRow++)
		{
			//Get row with test name and exist
			if(Sheet.getRow(iRow).getCell(iColPK).getStringCellValue().equalsIgnoreCase(dbName))
				break;
		}
		
		
		for(int i = 0;i < 3;i++) {
			dbDetails.put(Sheet.getRow(0).getCell(i).getStringCellValue(),Sheet.getRow(iRow).getCell(i).getStringCellValue());
			
		}
		
		return dbDetails;
		
	}
	
	
	//The below function is to fetch DB Query from Common
	public HashMapNew fFetchDBQuery(HashMapNew dataMap) {
		String sPK = dataMap.get("PK");
		final String pathFile = AutomationConstants.testDataPath + AutomationConstants.CommonFileName;
		final String DBSheet = "DB_SQL";
		final String PKColumn = "PK";
		
		int iColPK = fGetColumnIndex(pathFile, DBSheet, PKColumn);
		int iRowCnt = Sheet.getLastRowNum();
		
		int iRow;
		for(iRow=0;iRow<iRowCnt;iRow++)
		{
			//Get row with test name and exist
			if(Sheet.getRow(iRow).getCell(iColPK).getStringCellValue().equalsIgnoreCase(sPK))
				break;
		}
		
		if(iRow > iRowCnt){
			System.out.println("The PK: " + sPK + " not found in Common: ");
			//return false;
		}
		int[] arrSQL = new int[10];
		
		arrSQL[0] = fGetColumnIndex(pathFile, DBSheet, "SQL1");
		arrSQL[1] = fGetColumnIndex(pathFile, DBSheet, "SQL2");
		arrSQL[2] = fGetColumnIndex(pathFile, DBSheet, "SQL3");
		arrSQL[3] = fGetColumnIndex(pathFile, DBSheet, "SQL4");
		arrSQL[4] = fGetColumnIndex(pathFile, DBSheet, "SQL5");
		
		int i;
		String sSQL = "";
		
		for(i=0;i<5;i++) {
			if(Sheet.getRow(iRow).getCell(arrSQL[i])!= null) {
				sSQL = sSQL + Sheet.getRow(iRow).getCell(arrSQL[i]).getStringCellValue();
			}
		}
		
		//String sSQL = Sheet.getRow(iRow).getCell(iColSQL1).getStringCellValue() + " " + Sheet.getRow(iRow).getCell(iColSQL2).getStringCellValue()
						//+  " " + Sheet.getRow(iRow).getCell(iColSQL3).getStringCellValue() +  " " + Sheet.getRow(iRow).getCell(iColSQL4).getStringCellValue()
						//+ " " + Sheet.getRow(iRow).getCell(iColSQL5).getStringCellValue();
		
		if(sSQL.contentEquals("")) {	
			System.out.println("SQL for " + sPK + " is empty");
			//return false;
		}
		
		DBInfo.put("SQL", sSQL);
		
		String sExpRes = "";
		if(!sSQL.startsWith("UPDATE")){
			int iColExpRes = fGetColumnIndex(pathFile, DBSheet, "EXP_RESULTS");
			if(Sheet.getRow(iRow).getCell(iColExpRes) != null) {
				sExpRes = Sheet.getRow(iRow).getCell(iColExpRes).getStringCellValue();
			}
			//String sExpRes = Sheet.getRow(iRow).getCell(iColExpRes).getStringCellValue();
			
			DBInfo.put("EXP_RESULTS", sExpRes);
		}		
		
		//int iColTestName = fGetColumnIndex(calendarFile, mainSheet, testNameColumn);
		
		String sSaveTo = "";
		int iColExpRes = fGetColumnIndex(pathFile, DBSheet, "TO_SAVE");
		if(Sheet.getRow(iRow).getCell(iColExpRes) != null) {
			sSaveTo = Sheet.getRow(iRow).getCell(iColExpRes).getStringCellValue();
		}
		//String sExpRes = Sheet.getRow(iRow).getCell(iColExpRes).getStringCellValue();
		
		DBInfo.put("TO_SAVE", sSaveTo);	
		
		String sDBType = "";
		iColExpRes = fGetColumnIndex(pathFile, DBSheet, "DB_TYPE");
		if(Sheet.getRow(iRow).getCell(iColExpRes) != null) {
			sDBType = Sheet.getRow(iRow).getCell(iColExpRes).getStringCellValue();
		}
		//String sExpRes = Sheet.getRow(iRow).getCell(iColExpRes).getStringCellValue();
		
		DBInfo.put("DB_TYPE", sDBType);	
		
		return DBInfo;
	}
	
	
	//This method gets the data for the test name and the action passed to it
	public boolean fGetDataForTest(String calendarFile, String actionName, String testName){
		
		//DataSheet
		final String mainSheet = "MAIN";
		final String actionNameColumn = "ACTION";
		final String KeepReferSheet = "KEEP_REFER";
		final String testNameColumn = "TEST_NAME";
		
		//Clear Dictionary
		Dictionary.clear();
		orgDictionary.clear();
		KeysToWrite.clear();

		try{

			//Get column index of test name column
			int iColActionName = fGetColumnIndex(calendarFile, mainSheet, actionNameColumn);
			int iColTestName = fGetColumnIndex(calendarFile, mainSheet, testNameColumn);
			//Iterate through each rows from first sheet
			int iRowCnt = Sheet.getLastRowNum();

			//Loop
			int iRow;
			
			if(testName == "") {
				for(iRow=0;iRow<iRowCnt;iRow++)
				{
					//Get row with test name and exist
					if(Sheet.getRow(iRow).getCell(iColActionName).getStringCellValue().equalsIgnoreCase(actionName)) 
						break;
				}
			}else {
				for(iRow=0;iRow<iRowCnt;iRow++)
				{
					//Get row with test name and exist
					if(Sheet.getRow(iRow).getCell(iColActionName).getStringCellValue().equalsIgnoreCase(actionName) && Sheet.getRow(iRow).getCell(iColTestName).getStringCellValue().equalsIgnoreCase(testName)) 
						break;
				}
			}

			//Check if test found
			if(iRow == iRowCnt){
				System.out.println("Test with name: " + actionName + " not found in datasheet: " +  dataSheet);
				return false;
			}
		
			if(iRow < iRowCnt){ // it will execute only if the test case exists in the excel
				//Retrieve Skip value
				Dictionary.put("SKIP", Sheet.getRow(iRow-1).getCell(fGetColumnIndex(calendarFile, mainSheet, "SKIP")).getStringCellValue());
				if (Dictionary.get("SKIP").isEmpty())
				{
					//Set Header & DataRow
					Row headerRow = Sheet.getRow(iRow-1);
					Row dataRow = Sheet.getRow(iRow);

					//Get Column count for test-1 row
					int iParamCnt = headerRow.getLastCellNum();		 

					String key="";
					String value="";

					//Loop through params
					int iCol;
					for(iCol=0;iCol<iParamCnt;iCol++){

						//Fetch the value for the Header Row
						if (headerRow.getCell(iCol, org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK) == null)
						{
							key = "";
						}else{
							key = headerRow.getCell(iCol).getStringCellValue();
						}

						//Fetch the value for the Header Row
						if (dataRow.getCell(iCol, org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK) == null)
						{
							value = "";
						}else{
							value = dataRow.getCell(iCol).getStringCellValue();
							//Rahul's changes start here
							if(value!="") {
								if(value.charAt(0) == '&'){
									//Get column index of Key column
									int iColKey = fGetColumnIndex(calendarFile, KeepReferSheet, "KEY");
									//Get column index of Value column
									int iColVal = fGetColumnIndex(calendarFile, KeepReferSheet, "VALUE");
									
									String strKey = value.substring(1);
									int iRowCntKR = Sheet.getLastRowNum();

									//Loop
									int iRowKR;
									for(iRowKR=0;iRowKR<iRowCntKR+1;iRowKR++)
									{
										//Get row with test name and exist
										if(Sheet.getRow(iRowKR).getCell(iColKey).getStringCellValue().equalsIgnoreCase(strKey)) 
											break;
									}
									if(iRowKR == iRowCntKR+1) {
										value = "";
									}else {
										value = Sheet.getRow(iRowKR).getCell(iColVal).getStringCellValue();
									}									
								}else if(value.charAt(0) == '@' || value.charAt(0) == '$'){
									KeysToWrite.put(key,value.substring(1));
								}																						
							}
							//Rahul's changes end here						
						}
						
						//Check key value
						Dictionary.put(key,value);
						orgDictionary.put(key,value);
					} 	
				}
			}
			//Retrieve Skip value
			// Dictionary.put("SKIP", sheet.getRow(iRow-1).getCell(fGetColumnIndex(dataSheet, mainSheet, "SKIP_" + driverType)).getStringCellValue());

			return true;
		}
		catch(Exception e)
		{
			System.out.println("Exception " + e + " occured while fetching data from datasheet " + dataSheet + " for test " + actionName);
			return false;
		}	
		
	}	
	
	
	//This method is used to write data to Keep Refer
	public void fWriteKeepRefer(String calendarFile, HashMapNew KeysToWrite, HashMapNew Dictionary) {
		Row oRow;
		Cell oCell;
		final String KeepReferSheet = "KEEP_REFER";
		if(!KeysToWrite.isEmpty()) {
			int iKeysCnt = KeysToWrite.size();
			ArrayList<String> ToWriteList = new ArrayList<String>(KeysToWrite.values());
			//ArrayList<String> ArrListKeys = new ArrayList<String>();
			//HashMap <Integer, String> dKeysRows = new HashMap<Integer, String>();
			HashMapNew dKeysRows = new HashMapNew();
			//Get column index of Key column
			int iColKey = fGetColumnIndex(calendarFile, KeepReferSheet, "KEY");
			//Get column index of Value column
			int iColVal = fGetColumnIndex(calendarFile, KeepReferSheet, "VALUE");
			int iRowCnt = Sheet.getLastRowNum();
			int iRow;
			
			try {
				FileInputStream file = new FileInputStream(new File(calendarFile));		     
				//Get the workbook instance for XLS file 
				XSSFWorkbook workbook = new XSSFWorkbook(file);
				Sheet = workbook.getSheet(KeepReferSheet);
				//To store all the keys from Keep Refer in a dictionary
				for(iRow=1;iRow<=iRowCnt;iRow++) {
					Cell cell = Sheet.getRow(iRow).getCell(iColKey, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					//To check if the cell is blank to avoid null pointer exception
					if(cell.getCellType().name() != "BLANK") {
					dKeysRows.put(Integer.toString(iRow), Sheet.getRow(iRow).getCell(iColKey).getStringCellValue());
					}else {
						continue;
					}
				}
				
				int i;
				String Keyword = null;
				int iRowNum = 0;
				int flag = 0;
				for(i=0;i<iKeysCnt;i++) {
					String sKey = ToWriteList.get(i);
					for (Object o : KeysToWrite.keySet()) {
						if (KeysToWrite.get(o).equals(sKey)) {
							Keyword = o.toString();
						}
					}
					String sValue = Dictionary.get(Keyword);

					//To check if the key exists in KR, and to overwrite it
						for (Object o1 : dKeysRows.keySet()) {
							if (dKeysRows.get(o1).equals(sKey)) {
								//iRowNum = (Integer) o1;
								iRowNum = Integer.valueOf((String) o1);
								oRow = Sheet.getRow(iRowNum);
								oCell = oRow.getCell(iColVal);
								oCell.setCellValue(sValue);
								flag = 1;
							}
						}
						
					//This will be run if the key does not exist in Keep Refer
					if(flag!=1) {
						oRow = Sheet.getRow(iRowCnt+1);
						if(oRow==null) {
							Sheet.createRow(iRowCnt+1);
							oRow = Sheet.getRow(iRowCnt+1);
						}
						oCell = oRow.createCell(iColVal);
						//oCell = oRow.getCell(iColVal);
						oCell.setCellValue(sValue);
						
						oCell = oRow.createCell(iColKey);
						//oCell = oRow.getCell(iColVal);
						oCell.setCellValue(sKey);
					}
				}
				
					FileOutputStream outputStream = new FileOutputStream(calendarFile);
		            workbook.write(outputStream);
		            workbook.close();
		            outputStream.close();
			}
			catch(Exception e){
				System.out.println("Got exception while finding the Index column. Exception is " + e);
			}
		
		}
	}
	
	
	//This method gets the Column Index based on the column name passed
	public int fGetColumnIndex (String strXLSX, String strSheetName, String strColumnName){
		try
		{
			//Create the FileInputStream object			
			FileInputStream file = new FileInputStream(new File(strXLSX));		     
			//Get the workbook instance for XLS file 
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			//Get first sheet from the workbook
			Sheet = workbook.getSheet(strSheetName);

			//Iterate through each rows from first sheet
			Row row = Sheet.getRow(0);

			//Get the Column count
			int iColCount = row.getLastCellNum();
			int iCell = 0;
			int iIndex = -1;
			String strTemp = "";

			//Loop through all the columns
			for (iCell = 0; iCell < iColCount; iCell ++)
			{
				//Get the index for Version and Enviornment
				strTemp = Sheet.getRow(0).getCell(iCell).getStringCellValue().trim().toUpperCase();

				//if the strColumnName contains Header then check for HEADER or HEADER_IND
				if (strColumnName.equals("HEADER_IND") || strColumnName.equals("HEADER"))
				{
					if (strTemp.equals("HEADER") || strTemp.equals("HEADER_IND"))
					{
						iIndex = iCell;
						//Exit the Loop
						break;
					}

				}else{ 
					if (strTemp.equals(strColumnName.trim().toUpperCase()))
					{
						iIndex = iCell;
						//Exit the Loop
						break;
					}
				}
			}
			//Close the file
			file.close();

			//Validate if index is returned properly or not
			if (iIndex != -1)
			{
				return iIndex;

			}else{
				System.out.println("Failed to find the Column Id for Column " + strColumnName);
				return -1;
			}
		}catch (Exception e){
			System.out.println("Got exception while finding the Index column. Exception is " + e);
			return -1;
		}
	}
	
}
