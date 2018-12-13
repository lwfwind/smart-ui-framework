package com.ui.automation.framework.library.excel;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * General convenience methods for working with excel 2003/2007
 */
public class ExcelHelper {

    private final static Logger logger = Logger
            .getLogger(ExcelHelper.class);

    /**
     * The WorkBook path created in LoadFile
     */
    private transient String sWorkBookPath;

    /**
     * Region Excel WorkBook instance created in LoadFile
     */
    private transient Workbook workbook;

    /**
     * Region Excel WorkSheet instance created in LoadFile
     */
    private transient Sheet sheet;

    /**
     * Sets the region instances for Excel WorkBook and WorkSheet. These
     * instances for the Excel source are created only once and used by other
     * methods. NOTE: For any method to execute, LoadFile must be executed first
     * to set the WorkBook and WorkSheet.
     *
     * @param sWorkBook  Path to the Excel WorkBook
     * @param iWorkSheet Number of the WorkSheet
     * @return ExcelUtil excel util
     */
    public ExcelHelper loadFile(String sWorkBook, int iWorkSheet) {
        try {
            sWorkBookPath = sWorkBook;
            InputStream fis = new FileInputStream(sWorkBookPath);
            try {
                workbook = WorkbookFactory.create(fis);
            } catch (InvalidFormatException e) {
                logger.error(e.toString());
            }

            // setHssfsheet(getHssfworkbook().getSheetAt(iWorkSheet));
            sheet = getWorkbook().getSheetAt(iWorkSheet);
        } catch (IOException e) {
            logger.error(e.toString());
        }
        return this;
    }

    /**
     * Finds the number of used rows in the Excel WorkSheet
     *
     * @return The number of used rows
     */
    public int getUsedRowsCount() {
        return (getSheet().getLastRowNum() + 1);
    }

    /**
     * Finds the number of used columns in the Excel WorkSheet
     *
     * @return The number of used columns
     */
    public int getUsedColumnsCount() {
        int MaxColumnsCount = 0;
        for (int i = 0; i < getUsedRowsCount(); i++) {
            Row row = sheet.getRow(i);
            if (row != null && row.getLastCellNum() > MaxColumnsCount) {
                MaxColumnsCount = row.getLastCellNum();
            }

        }
        return MaxColumnsCount;

    }

    /**
     * Reads the value of a cell in an Excel WorkSheet
     *
     * @param iRow    Row Number
     * @param iColumn Column Number
     * @return Object cell value
     */
    public String getCellValue(int iRow, int iColumn) {
        String cellValue = null;
        Row row = sheet.getRow(iRow);
        if (row != null) {
            Cell cell = row.getCell(iColumn);
            if (cell != null) {
                cellValue = getCellValue(cell);
            }
        }
        if (cellValue == null) {
            return "";
        }
        return cellValue;

    }

    /**
     * Get cell value for excel
     *
     * @param cell Cell object of POI
     * @return string value
     */
    public String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
                    return sdf.format(date);
                }
                String retVal = String.valueOf(cell.getNumericCellValue());
                if (retVal.endsWith(".0")) {
                    // In order to fix integer number issue, e.g. 9 -> 9.0
                    int length = retVal.length() - 2;
                    retVal = retVal.substring(0, length);
                }
                if (retVal.contains("E")) {
                    // In order to fix big number
                    retVal = String.valueOf(cell.getNumericCellValue());
                    retVal = new BigDecimal(retVal).toString();
                }
                return retVal;
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_ERROR:
                return String.valueOf(cell.getErrorCellValue());
            case Cell.CELL_TYPE_FORMULA:
                return getFormulaValue(cell);
            default:
                return "";
        }
    }

    /**
     * Get formula type value
     *
     * @param cell The type of cell
     * @return string value
     */
    private String getFormulaValue(Cell cell) {
        String retVal = "";
        CellValue cellVal = null;

        try {
            HSSFWorkbook workbook = (HSSFWorkbook) cell.getRow().getSheet()
                    .getWorkbook();
            FormulaEvaluator evaluator = new HSSFFormulaEvaluator(workbook);
            cellVal = evaluator.evaluate(cell);
        } catch (Exception e) {
            XSSFWorkbook workbook = (XSSFWorkbook) cell.getRow().getSheet()
                    .getWorkbook();
            FormulaEvaluator evaluator = new XSSFFormulaEvaluator(workbook);
            cellVal = evaluator.evaluate(cell);
        }

        switch (cellVal.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                retVal = String.valueOf(cellVal.getBooleanValue());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                retVal = String.valueOf(cell.getNumericCellValue());
                if (retVal.endsWith(".0")) {
                    // In order to fix integer number issue, e.g. 9 -> 9.0
                    int length = retVal.length() - 2;
                    retVal = retVal.substring(0, length);
                }
                if (retVal.contains("E")) {
                    // In order to fix big number
                    retVal = String.valueOf(cell.getNumericCellValue());
                    retVal = new BigDecimal(retVal).toString();
                }
                break;
            case Cell.CELL_TYPE_STRING:
                retVal = cellVal.getStringValue();
                break;
            default:
                break;
        }

        return retVal;
    }

    /**
     * Returns a 2D array from the WorkSheet
     *
     * @return 2D array
     */
    public String[][] get2DArrayFromSheet() {
        String[][] Array2DFromSheet = new String[getUsedRowsCount()][getUsedColumnsCount()];
        try {
            for (int i = 0; i < getUsedRowsCount(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < getUsedColumnsCount(); j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null) {
                            Array2DFromSheet[i][j] = getCellValue(cell);
                        } else {
                            Array2DFromSheet[i][j] = "";
                        }

                    }
                } else {
                    for (int j = 0; j < getUsedColumnsCount(); j++) {
                        Array2DFromSheet[i][j] = "";
                    }
                }

            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return Array2DFromSheet;
    }

    /**
     * Returns a 2D array by specific rows and columns from the WorkSheet
     *
     * @param MaxRows    The specific max rows
     * @param MaxColumns The specific max columns
     * @return String[][] string [ ] [ ]
     */
    public String[][] get2DArrayFromSheetBySpecificRowsColumns(int MaxRows,
                                                               int MaxColumns) {
        String[][] Array2DFromSheet = new String[MaxRows][MaxColumns];
        try {
            for (int i = 0; i < MaxRows; i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < MaxColumns; j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null) {
                            Array2DFromSheet[i][j] = getCellValue(cell);
                        } else {
                            Array2DFromSheet[i][j] = "";
                        }

                    }
                } else {
                    for (int j = 0; j < MaxColumns; j++) {
                        Array2DFromSheet[i][j] = "";
                    }
                }

            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return Array2DFromSheet;
    }

    /**
     * Returns a 2D data from the WorkSheet
     *
     * @return the 2 d data from sheet
     */
    public List<List<String>> get2DDataFromSheet() {
        List<List<String>> data2DFromSheet = new ArrayList<List<String>>();
        List<String> rowData = new ArrayList<String>();
        try {
            for (int i = 0; i < getUsedRowsCount(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < getUsedColumnsCount(); j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null) {
                            rowData.add(getCellValue(cell));
                        } else {
                            rowData.add("");
                        }

                    }
                } else {
                    for (int j = 0; j < getUsedColumnsCount(); j++) {
                        rowData.add("");
                    }
                }
                data2DFromSheet.add(rowData);

            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return data2DFromSheet;
    }

    /**
     * Get the column data by the specified column.
     *
     * @param iColumn the column
     * @return the column data
     */
    public List<String> getColumnData(int iColumn) {
        List<String> columnData = new ArrayList<String>();
        String[][] data = get2DArrayFromSheet();
        for (String[] aData : data) {
            for (int j = 0; j < aData.length; j++) {
                if (j == iColumn) {
                    if (!aData[j].isEmpty()) {
                        columnData.add(aData[j]);
                    }
                }
            }
        }
        return columnData;
    }

    /**
     * Set the foreground fill color for specify cell
     *
     * @param iRow    Row Number
     * @param iColumn Column Number
     */
    public void setForegroundColor(int iRow, int iColumn) {
        Row row = sheet.getRow(iRow);
        Cell cell = row.getCell(iColumn);
        CellStyle style = workbook.createCellStyle();
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.CORAL.getIndex());
        if (cell != null) {
            cell.setCellStyle(style);
        } else {
            cell = row.createCell(iColumn);
            cell.setCellStyle(style);
        }
    }

    /**
     * Set the foreground fill color to white(default color) for specify cell
     *
     * @param iRow    Row Number
     * @param iColumn Column Number
     */
    public void setToDefaultForegroundColor(int iRow, int iColumn) {
        Row row = sheet.getRow(iRow);
        Cell cell = row.getCell(iColumn);
        CellStyle style = workbook.createCellStyle();
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());

        if (cell != null) {
            cell.setCellStyle(style);
        } else {
            cell = row.createCell(iColumn);
            cell.setCellStyle(style);
        }
    }

    /**
     * Get cell foreground color
     *
     * @param iRow    the row
     * @param iColumn the column
     * @return cell foreground color
     */
    public int getCellForegroundColor(int iRow, int iColumn) {
        Row row = sheet.getRow(iRow);
        Cell cell = row.getCell(iColumn);
        return cell.getCellStyle().getFillForegroundColor();
    }

    /**
     * Set the font fill color for specify cell
     *
     * @param iRow    Row Number
     * @param iColumn Column Number
     * @param color   the color
     */
    public void setFontColor(int iRow, int iColumn, long color) {
        Row row = sheet.getRow(iRow);
        Cell cell = row.getCell(iColumn);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor((short) color);
        style.setFont(font);
        if (cell != null) {
            cell.setCellStyle(style);
        } else {
            cell = row.createCell(iColumn);
            cell.setCellStyle(style);
        }
    }

    /**
     * Set the font fill color to red for specify cell
     *
     * @param iRow    Row Number
     * @param iColumn Column Number
     */
    public void setFontColorToRed(int iRow, int iColumn) {
        Row row = sheet.getRow(iRow);
        Cell cell = row.getCell(iColumn);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(IndexedColors.RED.getIndex());
        style.setFont(font);
        if (cell != null) {
            cell.setCellStyle(style);
        } else {
            cell = row.createCell(iColumn);
            cell.setCellStyle(style);
        }
    }

    /**
     * Set the font fill color for specify cell
     *
     * @param iRow    Row Number
     * @param iColumn Column Number
     */
    public void setToDefaultFontColor(int iRow, int iColumn) {
        Row row = sheet.getRow(iRow);
        Cell cell = row.getCell(iColumn);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);
        if (cell != null) {
            cell.setCellStyle(style);
        } else {
            cell = row.createCell(iColumn);
            cell.setCellStyle(style);
        }
    }

    /**
     * Get cell font color
     *
     * @param iRow    the row
     * @param iColumn the column
     * @return the cell font color
     */
    public int getCellFontColor(int iRow, int iColumn) {
        Row row = sheet.getRow(iRow);
        Cell cell = row.getCell(iColumn);
        CellStyle style = cell.getCellStyle();
        Font font = workbook.getFontAt(style.getFontIndex());
        return font.getColor();
    }

    /**
     * Set the cell value for specify cell
     *
     * @param iRow    Row Number
     * @param iColumn Column Number
     * @param value   value to be write
     */
    public void setCellValue(int iRow, int iColumn, String value) {
        Row row = sheet.getRow(iRow);
        if (row == null) {
            row = sheet.createRow(iRow);
        }
        Cell cell = row.getCell(iColumn);
        if (cell != null) {
            cell.setCellType(Cell.CELL_TYPE_BLANK);
            cell.setCellValue(value);
        } else {
            cell = row.createCell(iColumn);
            cell.setCellType(Cell.CELL_TYPE_BLANK);
            cell.setCellValue(value);
        }
    }

    /**
     * Save the workbook Sometimes writing twice to a .xlsx file throws
     * org.apache.xmlbeans.impl.values.XmlValueDisconnectedException
     * https://issues.apache.org/bugzilla/show_bug.cgi?id=49940
     */
    public void saveWorkbook() {
        // Write the output to a file
        try {
            FileOutputStream fileOut = new FileOutputStream(sWorkBookPath);
            workbook.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Set the cell type to numeric for specify cell
     *
     * @param iRow    Row Number
     * @param iColumn Column Number
     */
    public void setCellTypeToDate(int iRow, int iColumn) {
        Row row = sheet.getRow(iRow);
        Cell cell = row.getCell(iColumn);
        CellStyle style = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy"));
        cell.setCellStyle(style);
    }

    /**
     * Find a cell by the text
     *
     * @param text the text
     * @return cell cell
     */
    public Cell findCell(String text) {
        for (Row row : sheet) {
            if (row != null) {
                for (Cell cell : row) {
                    if (cell != null
                            && text.equalsIgnoreCase(getCellValue((Cell) cell).trim())) {
                        return cell;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Gets cell.
     *
     * @param rowIndex    the row index
     * @param columnIndex the column index
     * @return the cell
     */
    public Cell getCell(int rowIndex, int columnIndex) {
        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.getRow((int) rowIndex);
        // Create a cell and put a value in it.
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            cell = row.createCell(columnIndex);
            cell.setCellType(Cell.CELL_TYPE_BLANK);
        }
        return cell;

    }

    /**
     * Find a cell by the text in specified column
     *
     * @param iColumn the column
     * @param text    the text
     * @return the cell
     */
    public Cell findCellByColumn(int iColumn, String text) {
        for (Row row : sheet) {
            if (row != null) {
                Cell cell = (Cell) row.getCell(iColumn);
                if (cell != null
                        && text.equalsIgnoreCase(getCellValue((Cell) cell).trim())) {
                    return cell;
                }
            }

        }
        return null;
    }

    /**
     * Find a cell by the text in specified row
     *
     * @param iRow the row
     * @param text the text
     * @return cell cell
     */
    public Cell findCellByRow(int iRow, String text) {
        Row row = sheet.getRow(iRow);
        if (row != null) {
            for (Cell cell : row) {
                if (cell != null
                        && text.equalsIgnoreCase(getCellValue((Cell) cell).trim())) {
                    return cell;
                }
            }
        }
        return null;
    }

    /**
     * Build a test dictionary with header
     *
     * @param iHeadingRow the heading row
     * @param iRow        the row
     * @return hash map
     */
    public HashMap<String, String> buildRowHeadingDictionary(int iHeadingRow,
                                                             int iRow) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for (int iCol = 0; iCol < getUsedColumnsCount(); iCol++) {
            if (getCellValue(iRow, iCol) != null) {
                hashMap.put(getCellValue(iHeadingRow, iCol),
                        getCellValue(iRow, iCol));
            } else {
                hashMap.put(getCellValue(iHeadingRow, iCol), "");
            }

        }
        return hashMap;
    }

    /**
     * Build a dictionary list with header
     *
     * @param iHeadingRow the heading row
     * @return list list
     */
    public List<Map<String, String>> rowHeadingDictList(int iHeadingRow) {
        List<Map<String, String>> rowHeadingDictionaryList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < getUsedRowsCount(); i++) {
            rowHeadingDictionaryList.add(buildRowHeadingDictionary(iHeadingRow, i));
        }
        return rowHeadingDictionaryList;
    }

    /**
     * Build a test dictionary with header
     *
     * @param iHeadingRow       the heading row
     * @param strUniquecolValue the str uniquecol value
     * @return hash map
     */
    public HashMap<String, String> buildRowHeadingDictionary(int iHeadingRow,
                                                             String strUniquecolValue) {
        int iRow = findCell(strUniquecolValue).getRowIndex();
        return buildRowHeadingDictionary(iHeadingRow, iRow);
    }

    /**
     * Build a test dictionary with header
     *
     * @param iHeadingRow       the heading row
     * @param iColumn           the column
     * @param strUniquecolValue the str uniquecol value
     * @return hash map
     */
    public HashMap<String, String> buildRowHeadingDictionary(int iHeadingRow,
                                                             int iColumn, String strUniquecolValue) {
        try {
            int iRow = findCellByColumn(iColumn, strUniquecolValue)
                    .getRowIndex();
            return buildRowHeadingDictionary(iHeadingRow, iRow);
        } catch (Exception e) {
            logger.error("The " + strUniquecolValue + " can't be found in "
                    + (iColumn + 1) + " column in the file-" + sWorkBookPath, e);
        }
        return null;
    }

    /**
     * Create the workbook and work sheet For any method to execute, CreateExcel
     * must be executed first to set the WorkBook and WorkSheet.
     *
     * @param outputFile the output file
     * @param sheetName  the sheet name
     */
    public void createExcel(String outputFile, String sheetName) {
        sWorkBookPath = outputFile;
        if (sWorkBookPath.endsWith("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            workbook = new XSSFWorkbook();
        }
        sheet = workbook.createSheet(sheetName);
        // saveWorkbook();
    }

    /**
     * Create the cells for the worksheet
     *
     * @param rowsCount    the rows count
     * @param columnsCount the columns count
     */
    public void createCells(int rowsCount, int columnsCount) {
        for (int i = 0; i < rowsCount; i++) {
            // Create a row and put some cells in it. Rows are 0 based.
            Row row = sheet.createRow((int) i);
            for (int j = 0; j < columnsCount; j++) {
                // Create a cell and put a value in it.
                row.createCell(j);
            }
        }
    }

    /**
     * Create cell cell.
     *
     * @param rowIndex    the row index
     * @param columnIndex the column index
     * @return the cell
     */
    public Cell createCell(int rowIndex, int columnIndex) {
        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow((int) rowIndex);
        // Create a cell and put a value in it.
        return row.createCell(columnIndex);

    }

    /**
     * Create a hyperlink with blue or red font color for a specified cell based
     * on if it's a pass or failed case
     *
     * @param cell    the cell
     * @param address the address
     * @param isPass  the is pass
     */
    public void createHyperlink(Cell cell, String address, boolean isPass) {
        // cell style for hyperlinks
        CellStyle hlink_style = workbook.createCellStyle();
        Font hlink_font = workbook.createFont();
        hlink_font.setUnderline(Font.U_SINGLE);

        if (isPass) {
            hlink_font.setColor(IndexedColors.BLUE.getIndex());
        } else {
            hlink_font.setColor(IndexedColors.RED.getIndex());
        }

        hlink_style.setFont(hlink_font);

        writeHyperlinkToCell(cell, address, hlink_style);
    }

    /**
     * Writes hyper link to cell.
     *
     * @param cell       the <code>Cell<code> object of POI
     * @param linkedExpr the cell linked expression. example: #'sheetName'!A1
     */
    private void writeHyperlinkToCell(Cell cell, String linkedExpr,
                                      CellStyle cellStyle) {
        cell.setCellFormula("HYPERLINK(\"" + linkedExpr + "\", \""
                + getCellValue(cell) + "\")");
        cell.setCellType(Cell.CELL_TYPE_FORMULA);
        cell.setCellStyle(cellStyle);
    }

    /**
     * Writes hyper link to cell.
     *
     * @param cell          the <code>Cell</code> object of POI
     * @param linkedSheet   the linked sheet name
     * @param linkedPostion the linked position example: A1, A2, A3...
     * @param cellStyle     the cell style
     */
    public void writeHyperlinkToCell(Cell cell, String linkedSheet,
                                     String linkedPostion, CellStyle cellStyle) {
        String linkExpr = "#'" + linkedSheet + "'!" + linkedPostion;
        writeHyperlinkToCell(cell, linkExpr, cellStyle);
    }

    /**
     * insert row into the sheet, the style of cell is the same as startRow
     *
     * @param startRow the start row
     * @param rows     the rows
     */
    public void insertRow(int startRow, int rows) {
        int startRowIndex = startRow;
        sheet.shiftRows(startRowIndex + 1, sheet.getLastRowNum(), rows, true,
                false);
        // Parameters:
        // startRow - the row to start shifting
        // endRow - the row to end shifting
        // n - the number of rows to shift
        // copyRowHeight - whether to copy the row height during the shift
        // resetOriginalRowHeight - whether to set the original row's height to
        // the default

        for (int i = 0; i < rows; i++) {
            sheet.createRow(++startRowIndex);
        }
    }

    /**
     * Remove a row by its index
     *
     * @param rowIndex the row index
     */
    public void removeRow(int rowIndex) {
        int lastRowNum = sheet.getLastRowNum();
        if (rowIndex >= 0 && rowIndex < lastRowNum) {
            sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
        }
        if (rowIndex == lastRowNum) {
            Row removingRow = sheet.getRow(rowIndex);
            if (removingRow != null) {
                sheet.removeRow(removingRow);
            }
        }

    }

    /**
     * Export the ListMap to excel with header row
     *
     * @param listMap    the list map
     * @param outputFile the output file
     * @param sheetName  the sheet name
     */
    public void exportListMapToExcel(List<Map<String, String>> listMap,
                                     String outputFile, String sheetName) {
        this.createExcel(outputFile, sheetName);
        this.createCells(listMap.size() + 1, listMap.get(0).size());
        logger.info("Start to export to the result file " + outputFile);
        int iRow = 0;
        int iCol = 0;
        // Fill the header row
        for (Map.Entry<String, String> entry : listMap.get(0).entrySet()) {
            this.setCellValue(0, iCol, entry.getKey());
            iCol++;
        }
        // Fill the data row
        for (Map<String, String> map : listMap) {
            iRow++;
            iCol = 0;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                this.setCellValue(iRow, iCol, entry.getValue());
                iCol++;
            }
        }
        // Save
        this.saveWorkbook();
        logger.info("The export process are done!");
    }

    /**
     * Gets workbook.
     *
     * @return the workbook
     */
    public Workbook getWorkbook() {
        return workbook;
    }

    /**
     * Sets workbook.
     *
     * @param workbook the workbook
     */
    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    /**
     * Gets sheet.
     *
     * @return the sheet
     */
    public Sheet getSheet() {
        return sheet;
    }

    /**
     * Sets sheet.
     *
     * @param sheet the sheet
     */
    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    /**
     * Gets work book path.
     *
     * @return the work book path
     */
    public String getsWorkBookPath() {
        return sWorkBookPath;
    }

    /**
     * Sets work book path.
     *
     * @param sWorkBookPath the s work book path
     */
    public void setsWorkBookPath(String sWorkBookPath) {
        this.sWorkBookPath = sWorkBookPath;
    }
}
