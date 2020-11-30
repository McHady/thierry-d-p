package matcher;

import matcher.perform.PerformManager;
import matcher.url.UrlGenerator;

import java.io.IOException;
import java.util.concurrent.Executors;



public class Main {


    public static void main(String[] args){

        var config = new Configuration("config.ini");
        var urlGenerator = new UrlGenerator(config.getUrlRoot());
        var fileManager = new FileManager(config);

        var wsjFiles = fileManager.getWSJFiles();
        var resultFiles = fileManager.getResultFiles();

        var service = Executors.newFixedThreadPool(wsjFiles.size());
        for (var wsjFile: wsjFiles) {

            var future = service.submit(() -> {

                System.out.printf("file: %s%n", wsjFile);
                try (var wsjExcel = new ExcelManager(wsjFile)) {

                    var wsjRowNumber = wsjExcel.getRowNumber();
                    var country = fileManager.getCountryByFileName(wsjFile);
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
                                PerformManager.perform(config.getResultFileSuffix(), url, resultExcel, country, wsjCode, tableKey);
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
}
