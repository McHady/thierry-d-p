package matcher.perform.wsj;

import com.google.gson.JsonObject;
import matcher.ExcelManager;
import org.apache.poi.ss.usermodel.CellType;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class LastPricePerformer extends WSJPerformer<String> {

    @Override
    protected String getJsonData(JsonObject jsonObject) {
        return jsonObject.get("data").getAsJsonObject().get("quote").getAsJsonObject().get("topSection").getAsJsonObject().get("value").getAsString();
    }

    @Override
    protected void writeData(ExcelManager excelManager, String data, String country, String wsjCode, String tableKey) {

        var gerNumberFormat = NumberFormat.getNumberInstance(Locale.UK);
        double price = 0;
        try {
            price = gerNumberFormat.parse(data).doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.printf("Country: %s, Company: %s, Price: %s%n", country, wsjCode, price);

        var resultRowIndex = excelManager.getRowNumberByCellValue("B", tableKey);
        excelManager.setCellValue(resultRowIndex, "M", price);
    }
}

