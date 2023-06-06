package logic.server.service.impl;

import logic.server.service.IToolService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

@Slf4j
@Service
public class ToolServiceImpl implements IToolService {
    @Value("${spring.datasource.url}")
    String jdbcURL;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;
    @Override
    public void getExcel(String path){
        File file = new File(path);
        String tableName = "test";
        try {
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            Statement myStmt = connection.createStatement();
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workBook = new XSSFWorkbook(inputStream);
            Sheet sheet = workBook.getSheetAt(0);
            int firstRow = sheet.getFirstRowNum();
            int lastRow = sheet.getLastRowNum();
            String indexValues = "";
            for (int index = firstRow; index <= lastRow; index++) {
                Row row = sheet.getRow(index);
                String cellValues = "";
                for (int cellIndex = row.getFirstCellNum(); cellIndex < row.getLastCellNum(); cellIndex++) {
                    Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    if(index == 0){
                        indexValues += cell.getStringCellValue();
                        if(cellIndex + 1 != row.getLastCellNum()) {
                           indexValues += ", ";
                        }
                    }
                    else {
                        cellValues = addCellValue(cellValues, cell);
                        if(cellIndex + 1 != row.getLastCellNum()){
                            cellValues += ", ";
                        }
                    }
                }
                if(index != 0){
                    String sql = "INSERT INTO " + tableName + "(" + indexValues + ")" + " VALUES (" + cellValues + ")";
                    System.out.println(sql);
                }
//                myStmt.executeUpdate(sql);
            }
            connection.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String addCellValue(String cellValues,Cell cell) {
        CellType cellType = cell.getCellType().equals(CellType.FORMULA)
                ? cell.getCachedFormulaResultType() : cell.getCellType();
        if (cellType.equals(CellType.STRING)) {
            cellValues += "\'" + cell.getStringCellValue() + "\'";
        }
        if (cellType.equals(CellType.NUMERIC)) {
            if (DateUtil.isCellDateFormatted(cell)) {
                cellValues += cell.getDateCellValue();
            } else {
                cellValues += cell.getNumericCellValue();
            }
        }
        if (cellType.equals(CellType.BOOLEAN)) {
            cellValues += cell.getBooleanCellValue();
        }
        return cellValues;
    }


}
