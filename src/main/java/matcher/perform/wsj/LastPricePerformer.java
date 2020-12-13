package matcher.perform.wsj;

import com.google.gson.JsonObject;
import matcher.ExcelManager;
import org.apache.poi.ss.usermodel.CellType;

public class LastPricePerformer extends WSJPerformer<String> {

    @Override
    protected String getJsonData(JsonObject jsonObject) {
        return jsonObject.get("data").getAsJsonObject().get("quote").getAsJsonObject().get("topSection").getAsJsonObject().get("value").getAsString();
    }

    @Override
    protected void writeData(ExcelManager excelManager, String data, String country, String wsjCode, String tableKey) {
        var pieces = data.split("\\.");
        var priceBuilder = new StringBuilder();
        for (var p = 0; p < pieces.length; p++) {

            if (p == 0){

                priceBuilder.append(pieces[p]);
            }
            else if (p == pieces.length-1){
                priceBuilder.append(",").append(pieces[p]);
            }
            else
            {
                priceBuilder.append(".").append(pieces[p]);
            }

        }
        var price = priceBuilder.toString();
        System.out.printf("Country: %s, Company: %s, Price: %s%n", country, wsjCode, price);

        var resultRowIndex = excelManager.getRowNumberByCellValue("B", tableKey);
        excelManager.setCellValue(resultRowIndex, "M", price, CellType.STRING);
    }
}

