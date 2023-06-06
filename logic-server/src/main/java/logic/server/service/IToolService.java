package logic.server.service;

import org.apache.poi.ss.usermodel.Cell;

import java.util.ArrayList;

public interface IToolService {
    void getExcel(String path);

    String addCellValue(String cellValues, Cell cell);
}
