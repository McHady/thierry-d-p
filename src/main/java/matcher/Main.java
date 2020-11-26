package matcher;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.poi.ss.usermodel.CellType;
import org.jsoup.Jsoup;
import java.io.IOException;



public class Main {


    public static void main(String[] args){

        var config = new Configuration("config.ini");

        try (var wsjExcel = new ExcelManager(config.getWSJTableFile())) {

            var wsjRowNumber = wsjExcel.getRowNumber();
            try (var resultExcel = new ExcelManager(config.getResultTableFile())){

                for (var i = 1; i < wsjRowNumber; i++){

                    System.out.println("==========================================");
                    System.out.printf("Taking data from WSJ's row #%d%n", i +1);
                    var countryCode = wsjExcel.getCellValue(i, config.getWSJCountryCodeColumn());
                    var prefix = wsjExcel.getCellValue(i, config.getWSJPrefixColumn());
                    var wsjCode = wsjExcel.getCellValue(i, config.getWSJCodeColumn());
                    var tableKey = wsjExcel.getCellValue(i, config.getWSJKeyColumn());

                    if (tableKey == null)
                        break;

                    var url = String.format(
                            "%s%s/%s/%s?" +
                                    "id=%%7B\"ticker\"%%3A\"%s\"%%2C\"" +
                                    "countryCode\"%%3A\"%s\"%%2C\"exchange\"%%3A\"%s\"%%2C\"type\"%%3A\"STOCK\"%%2C\"path\"%%3A\"%%2Fat%%2F%S%%2Fubs\"%%7D&type=quotes_chart",
                            config.getUrlRoot(), countryCode, prefix, wsjCode, wsjCode.toLowerCase(), countryCode.toLowerCase(), prefix.toLowerCase(), prefix.toLowerCase());
                    try {
                        var price = loadPriceByCSSSelector(url).replace(".", ",");

                        System.out.printf("Price: %s%n", price);

                        var resultRowIndex = resultExcel.getRowNumberByCellValue(config.getResultKeyColumn(), tableKey);
                        resultExcel.setCellValue(resultRowIndex, config.getResultDestinationColumn(), price, CellType.STRING);
                    } catch (Exception e) {
                        System.out.printf("Can't get URL %s", url);
                        e.printStackTrace();
                    }
                    System.out.println("==========================================");
                }
                resultExcel.update();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String loadPriceByCSSSelector(String url) throws IOException {

        var json = Jsoup.connect(url).ignoreContentType(true).get().text();

        var jObject = (JsonObject) new JsonParser().parse(json);
        return jObject.get("data").getAsJsonObject().get("quote").getAsJsonObject().get("topSection").getAsJsonObject().get("value").getAsString();
    }
}
