package matcher;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import matcher.url.UrlGenerator;
import org.apache.poi.ss.usermodel.CellType;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class Main {


    public static void main(String[] args){

        var config = new Configuration("config.ini");
        var urlGenerator = new UrlGenerator(config.getUrlRoot(), null);
        var wsjFiles = Arrays.stream(Objects.requireNonNull(new File(config.getWSJFileFolder()).listFiles()))
                .filter(file -> file.isFile() && file.getName().contains("list.xlsx")).map(file -> config.getWSJFileFolder() + "/" + file.getName()).collect(Collectors.toList());
        System.out.println(String.join(", ", wsjFiles));

        var resultFiles = Arrays.stream(Objects.requireNonNull(new File(config.getResultFileFolder()).listFiles()))
                .filter(file -> file.isFile() && file.getName().contains("3-result.xlsx")).map(file -> config.getResultFileFolder() + "/" + file.getName()).collect(Collectors.toList());
        System.out.println(String.join(", ", resultFiles));

        var service = Executors.newFixedThreadPool(wsjFiles.size());
        for (var wsjFile: wsjFiles) {

            var future = service.submit(() -> {

                System.out.printf("file: %s%n", wsjFile);
                try (var wsjExcel = new ExcelManager(wsjFile)) {

                    var wsjRowNumber = wsjExcel.getRowNumber();
                    var country = new File(wsjFile).getName().replace(" list.xlsx", "");
                    var resultFile = resultFiles.stream().filter(filename -> filename.contains(country)).findFirst().get();
                    try (var resultExcel = new ExcelManager(resultFile)){
                        for (var i = 1; i < wsjRowNumber; i++){

                            var countryCode = wsjExcel.getCellValue(i, config.getWSJCountryCodeColumn());
                            var prefix = wsjExcel.getCellValue(i, config.getWSJPrefixColumn());
                            var wsjCode = wsjExcel.getCellValue(i, config.getWSJCodeColumn());
                            var tableKey = wsjExcel.getCellValue(i, config.getWSJKeyColumn());

                            if (tableKey == null)
                                continue;

                            var url = urlGenerator.generate(countryCode, prefix, wsjCode);
                            try {
                                var price = loadPriceFromUrl(url);

                                var pieces = price.split("\\.");
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
                                price = priceBuilder.toString();
                                System.out.printf("Country: %s, Company: %s, Price: %s%n", country, wsjCode, price);

                                var resultRowIndex = resultExcel.getRowNumberByCellValue(config.getResultKeyColumn(), tableKey);
                                resultExcel.setCellValue(resultRowIndex, config.getResultDestinationColumn(), price, CellType.STRING);
                            } catch (Exception e) {
                                System.out.printf("Can't get URL %s", url);
                                e.printStackTrace();
                            }
                            resultExcel.update();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }
        service.shutdown();

    }

    private static String loadPriceFromUrl(String url) throws IOException {

        var json = Jsoup.connect(url).ignoreContentType(true).get().text();

        var jObject = (JsonObject) new JsonParser().parse(json);
        return jObject.get("data").getAsJsonObject().get("quote").getAsJsonObject().get("topSection").getAsJsonObject().get("value").getAsString();
    }
}
