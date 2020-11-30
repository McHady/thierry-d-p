package matcher;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;

public class ExcelManager implements Closeable {

    private XSSFWorkbook workBook;
    private final String filename;

    public ExcelManager(String filename) {

        this.filename = filename;
        try (var stream = new FileInputStream(filename)){

             this.workBook = new XSSFWorkbook(stream);

        } catch (FileNotFoundException e) {
            System.out.printf("Can't find file %s%n", filename);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private XSSFCell getCell(Integer sheetIndex, Integer rowIndex, String columnName){
        return getCell(sheetIndex, rowIndex, columnName, false);
    }
    private XSSFCell getCell(Integer sheetIndex, Integer rowIndex, String columnName, Boolean create){

        var sheet = this.workBook.getSheetAt(sheetIndex);
        var row = sheet.getRow(rowIndex);

        var cellIter = row.cellIterator();
        while (cellIter.hasNext()) {
            var cell = (XSSFCell) cellIter.next();

            if (CellReference.convertNumToColString(cell.getColumnIndex()).equalsIgnoreCase(columnName)) {
                return cell;
            }
        }

        if (create)
            return row.createCell(CellReference.convertColStringToIndex(columnName));
        else
            return null;
    }

    public String getCellValue(Integer rowIndex, String columnName){
        return  this.getCellValue(0, rowIndex, columnName);
    }

    public String getCellValue(Integer sheetIndex, Integer rowIndex, String columnName) {

        var cell = this.getCell(sheetIndex, rowIndex, columnName);

        if (cell != null){

            switch (cell.getCellType()) {

                case _NONE:
                case FORMULA:
                case BLANK:
                    return null;
                case NUMERIC:
                {
                    var value = cell.getNumericCellValue();
                    if (value - (int)value == (double) 0)
                        return Integer.toString((int)value);
                    else
                        return Double.toString(value);
                }
                case STRING:
                    return cell.getStringCellValue();
                case BOOLEAN:
                    return Boolean.toString( cell.getBooleanCellValue() );
                case ERROR:
                    return cell.getErrorCellString();
            }
        }
        return  null;
    }
    public void setCellValue(Integer rowIndex, String columnName, String value){
        this.setCellValue(0, rowIndex, columnName, value);
    }
    public void setCellValue(Integer sheetIndex, Integer rowIndex, String columnName, String value){
        this.setCellValue(sheetIndex, rowIndex, columnName, value, CellType.NUMERIC);
    }
    public void setCellValue(Integer rowIndex, String columnName, String value, CellType cellType){
        this.setCellValue(0, rowIndex, columnName, value, cellType);
    }
    public void setCellValue(Integer sheetIndex, Integer rowIndex, String columnName, String value, CellType cellType) {

        var cell = this.getCell(sheetIndex, rowIndex, columnName, true);

        cell.setCellValue(value);
        cell.setCellType(cellType);
    }

    public void setCellValue(Integer rowIndex, String columnName, Double value) {
        setCellValue(0, rowIndex, columnName, value);
    }
    public void setCellValue(Integer sheetIndex, Integer rowIndex, String columnName, Double value){
        var cell = this.getCell(sheetIndex, rowIndex, columnName, true);
        cell.setCellValue(value);
        cell.setCellType(CellType.NUMERIC);
        var style = this.workBook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.0"));
        cell.setCellStyle(style);
    }

    public  Integer getRowNumberByCellValue(String columnName, String value){
        return this.getRowNumberByCellValue(0, columnName, value);
    }

    public Integer getRowNumberByCellValue(Integer sheetIndex, String columnName, String value){

        var sheet = this.workBook.getSheetAt(sheetIndex);
        var rowIter = sheet.rowIterator();

        var index = 0;
        while(rowIter.hasNext()){
            var row = (XSSFRow)rowIter.next();

            var cellIndex = CellReference.convertColStringToIndex(columnName);

            if (row.getCell(cellIndex).getStringCellValue().equalsIgnoreCase(value))
                return index;

            index++;
        }

        return -1;
    }

    public Integer getRowNumber(){
        return this.getRowNumber(0);
    }
    public Integer getRowNumber(Integer sheetIndex){
        var sheet = this.workBook.getSheetAt(sheetIndex);
        return sheet.getLastRowNum() - (sheet.getFirstRowNum() - 1);
    }

    public void update(){
        try(var stream = new FileOutputStream(new File(this.filename))){
            this.workBook.write(stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        this.workBook.close();
    }
}
