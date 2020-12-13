package matcher.perform.wsj;

import com.google.gson.JsonObject;
import matcher.ExcelManager;

public class SharesOutstandingPublicFloatPerformer extends WSJPerformer<DataPair> {

    @Override
    protected DataPair getJsonData(JsonObject jsonObject) {

        var dataRoot = jsonObject.get("data").getAsJsonObject().get("quoteData").getAsJsonObject().get("Financials").getAsJsonObject();
        var soRoot =  dataRoot.get("SharesOutstanding");
        var sharesOutstanding= soRoot.isJsonNull() ? null : soRoot.getAsDouble();
        var publicFloat = dataRoot.get("PublicFloat").getAsDouble();

        return new DataPair(sharesOutstanding, publicFloat);
    }

    @Override
    protected void writeData(ExcelManager excelManager, DataPair data, String country, String wsjCode, String tableKey) {


        var divisionResult =  data.getSharesOutstanding() != null ? data.getPublicFloat() / (data.getSharesOutstanding()) : data.getPublicFloat();


        System.out.printf("Country: %s, Company: %s, Shares Outstanding: %f, Public Float / Shares Outstanding: %f%n", country, wsjCode, data.getSharesOutstanding(), divisionResult);

        var resultRowIndex = excelManager.getRowNumberByCellValue("B", tableKey);

        if (data.getSharesOutstanding() != null)
            excelManager.setCellValue(resultRowIndex, "AD", data.getSharesOutstanding());
        excelManager.setCellValue(resultRowIndex, "AE", divisionResult);
    }
}
