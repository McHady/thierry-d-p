package matcher.perform.wsj;

import matcher.ExcelManager;

import java.io.IOException;
import java.util.Objects;

public class WSJPerformManager {

    public  static void perform(String resultSuffix, String url, ExcelManager excelManager, String country, String wsjCode, String tableKey) throws IOException {
        perform(Objects.requireNonNull(TaskType.getTypeBySuffix(resultSuffix)), url, excelManager, country, wsjCode, tableKey);
    }
    public static void perform(TaskType type, String url, ExcelManager excelManager, String country, String wsjCode, String tableKey) throws IOException {
        type.getPerformer().perform(url, excelManager, country, wsjCode, tableKey);
    }
}

