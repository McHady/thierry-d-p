package matcher.script;

import matcher.Configurationable;
import matcher.ElectronicDocument;
import matcher.ExcelManager;
import matcher.FileManager;
import matcher.perform.wsj.WSJPerformManager;
import matcher.url.UrlGenerator;

import java.io.IOException;
import java.util.List;

public class WSJScript extends InputFileListAsyncHandlingScript {

    private final String urlRoot = "https://www.wsj.com/market-data/quotes/";

    public WSJScript(Configurationable config) {
        super(config);
    }

    @Override
    protected Boolean ignoreFileNameParts() {
        return false;
    }

    @Override
    protected UrlGenerator getUrlGenerator() {
        return new UrlGenerator(this.urlRoot);
    }

    @Override
    protected void cycleIteration(FileManager fileManager, UrlGenerator urlGenerator, String inputFile) {
    }

    @Override
    protected void cycleIteration(FileManager fileManager, UrlGenerator urlGenerator, String inputFile, List<String> resultFiles) {
        this.internalWSJIteration(fileManager, urlGenerator, inputFile, resultFiles);
    }

    private void internalWSJIteration(FileManager fileManager, UrlGenerator urlGenerator, String wsjFile, List<String> resultFiles){

        System.out.printf("file: %s%n", wsjFile);
        try (var wsjExcel = new ExcelManager(wsjFile)) {

            var wsjRowNumber = wsjExcel.getRowNumber();
            var country = fileManager.getCountryByFileName(wsjFile);
            var fileNameOptional = resultFiles.stream().filter(filename -> filename.contains(country)).findFirst();
            var resultFile = fileNameOptional.orElse(null);
            try (var resultExcel = this.buildResultExcelFile(resultFile, wsjExcel, country)){
                for (var i = 1; i < wsjRowNumber; i++){

                    if (wsjExcel != null) {
                        var countryCode = wsjExcel.getCellValue(i, config.getWSJCountryCodeColumn());
                        var prefix = wsjExcel.getCellValue(i, config.getWSJPrefixColumn());
                        var wsjCode = wsjExcel.getCellValue(i, config.getWSJCodeColumn());
                        var tableKey = wsjExcel.getCellValue(i, config.getWSJKeyColumn());

                        if (tableKey == null)
                            continue;

                        var url = urlGenerator.generate(countryCode, prefix, wsjCode);

                        try {
                            WSJPerformManager.perform(config.getResultFileSuffix(), url, resultExcel, country, wsjCode, tableKey);
                        } catch (Exception e) {
                            System.out.printf("Can't get URL from row #%d - %s", i+1, url);
                            e.printStackTrace();
                        }
                        resultExcel.update();
                        }
                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

    }

    protected ExcelManager buildResultExcelFile(String fileName, ElectronicDocument originFile, String country) throws NullPointerException {
        if (fileName != null)
            return new ExcelManager(fileName);
        else
            throw new NullPointerException("File name not specified");
    }

    @Override
    protected Boolean isResultFilesSame() {
        return false;
    }
}

