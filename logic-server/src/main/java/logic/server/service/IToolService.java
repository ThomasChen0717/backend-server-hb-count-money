package logic.server.service;

import java.util.List;

public interface IToolService {
    List<String> updateFromExcel(String path, List<String> fileNames);
}
