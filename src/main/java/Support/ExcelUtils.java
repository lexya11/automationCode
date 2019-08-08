package Support;

import Environments.Constants;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtils {
    private static XSSFSheet ExcelWSheet;
    private static XSSFWorkbook ExcelWBook;
    private static org.apache.poi.ss.usermodel.Cell Cell;
    private static XSSFRow Row;

    // Log variables
    static Logger Log = Logger.getLogger(SetupServer.class);

    /**
     * initiate excel file with path
     * @param Path
     */
    public static void setExcelFile(String Path) {
        try {
            //System.out.println("Path" + Path);
            FileInputStream ExcelFile = new FileInputStream(Path);
            ExcelWBook = new XSSFWorkbook(ExcelFile);
        } catch (Exception e){
            Log.error("Class Utils | Method setExcelFile | Exception desc : "+e.getMessage());
            System.out.println("Error setExcelFile");
        }
    }

    public static Object[][] getTableArray(String FilePath, String SheetName) throws Exception {
        String[][]tableArray = null;
        try {
            FileInputStream ExcelFile = new FileInputStream(FilePath);
            // Access the required test data sheet
            ExcelWBook = new XSSFWorkbook(ExcelFile);
            ExcelWSheet = ExcelWBook.getSheet(SheetName);
            int startRow = 1;
            int startCol = 0;
            int totalCols = ExcelWSheet.getRow(0).getLastCellNum();
//            System.out.println(totalCols);
            int totalRows = ExcelWSheet.getLastRowNum();
//            System.out.println(totalRows);

            tableArray = new String[totalRows][totalCols];
            for (int i=startRow;i<=totalRows;i++) {
                for (int j=startCol;j<totalCols;j++){
                    tableArray[i-1][j] = getCellData(i,j,ExcelWSheet.getSheetName());
//                    System.out.println(tableArray[i-1][j]);
                }
            }
        }
        catch (FileNotFoundException e){
            Log.error("Class Utils | Method getTableArray | Exception desc : "+e.getMessage());
            System.out.println("Could not read the Excel sheet");
        }
        catch (IOException e){
            Log.error("Class Utils | Method getTableArray | Exception desc : "+e.getMessage());
            System.out.println("Could not read the Excel sheet");
        }
        return(tableArray);
    }

    /**
     * get  value in specific column and row from sheetname
     * @param RowNum
     * @param ColNum
     * @param SheetName
     * @return String
     */
    public static String getCellData(int RowNum, int ColNum, String SheetName ) {
        try{
            ExcelWSheet = ExcelWBook.getSheet(SheetName);
            Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
            String CellData = (Cell != null) ? Cell.toString() : "";
            return CellData;
        }catch (Exception e){
            Log.error("Class Utils | Method getCellData | Exception desc : "+e.getMessage());
            System.out.println("Error getCellData");
            return "";
        }
    }

    /**
     * get total Row Count in any Sheet name excel
     * @param SheetName
     * @return
     */
    public static int getRowCount(String SheetName){
        int iNumber=0;
        try {
            ExcelWSheet = ExcelWBook.getSheet(SheetName);
            iNumber=ExcelWSheet.getLastRowNum()+1;
        } catch (Exception e){
            Log.error("Class Utils | Method getRowCount | Exception desc : "+e.getMessage());
            System.out.println("Error getRowCount");
        }
        return iNumber;
    }

    /**
     * Check if value at [row][Cell] in excel/sheet with criteria String
     * and return the row which match the criteria
     * @param sTestCaseName : Test Case name as String
     * @param colNum : column number
     * @param SheetName
     * @return int
     */
    public static int getRowContains(String sTestCaseName, int colNum,String SheetName) {
        int iRowNum=0;
        try {
            //ExcelWSheet = ExcelWBook.getSheet(SheetName);
            int rowCount = ExcelUtils.getRowCount(SheetName);
            for (; iRowNum<rowCount; iRowNum++){
                if  (ExcelUtils.getCellData(iRowNum,colNum,SheetName).equalsIgnoreCase(sTestCaseName)){
                    break;
                }
            }
        } catch (Exception e){
            Log.error("Class Utils | Method getRowContains | Exception desc : "+e.getMessage());
            System.out.println("Error getRowContains");
        }
        return iRowNum;
    }

    /**
     * get last Test step  for Test case in the test step sheet
     * @param SheetName
     * @param sTestCaseID
     * @param iTestCaseStart
     * @return int
     */
    public static int getTestStepsCount(String SheetName, String sTestCaseID, int iTestCaseStart) {
        int totalRow = ExcelUtils.getRowCount(SheetName);
        System.out.println("get last Test step  for Test case in the test step sheet ");
        try {

            for(int i=iTestCaseStart;i<=totalRow;i++){
                if(!sTestCaseID.equals(ExcelUtils.getCellData(i, 12 /*Constants.Col_TestCaseID*/, SheetName))){
                    int number = i;
                    return number;
                }
            }
            ExcelWSheet = ExcelWBook.getSheet(SheetName);
            int number=ExcelWSheet.getLastRowNum()+1;
            return number;
        } catch (Exception e){
            Log.error("Class Utils | Method getRowContains | Exception desc : "+e.getMessage());
            System.out.println("Error getTestStepsCount");
            return 0;
        }
    }

    /**
     * set value to specific column and row in sheetname
     * @param Result
     * @param RowNum
     * @param ColNum
     * @param SheetName
     */
    @SuppressWarnings("static-access")
    public static void setCellData(String Result,  int RowNum, int ColNum, String SheetName)  {
        try{

            ExcelWSheet = ExcelWBook.getSheet(SheetName);
            Row  = ExcelWSheet.getRow(RowNum);
            Cell = Row.getCell(ColNum, Row.RETURN_BLANK_AS_NULL);
            if (Cell == null) {
                Cell = Row.createCell(ColNum);
                Cell.setCellValue(Result);
            } else {
                Cell.setCellValue(Result);
            }
            FileOutputStream fileOut = new FileOutputStream("."+Constants.SIGNIN_UP_DATA);
            ExcelWBook.write(fileOut);
            //fileOut.flush();
            fileOut.close();
            ExcelWBook = new XSSFWorkbook(new FileInputStream("."+Constants.SIGNIN_UP_DATA));
        }catch(Exception e){
            System.out.println("Error setCellData");
        }
    }
}