package matcher.script;

import matcher.Configurationable;
import matcher.ExcelManager;
import matcher.FileManager;
import matcher.perform.wsj.WSJPerformManager;
import matcher.url.UrlGenerator;

import java.io.IOException;
import java.util.List;

public class WSJScript extends InputFileListAsyncHandlingScript {
    public WSJScript(Configurationable config) {
        super(config);
    }

    @Override
    protected Boolean ignoreFileNameParts() {
        return false;
    }

    @Override
    protected UrlGenerator getUrlGenerator() {
        return new UrlGenerator(this.config.getUrlRoot());
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
                        WSJPerformManager.perform(config.getResultFileSuffix(), url, resultExcel, country, wsjCode, tableKey);
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
    }

    @Override
    protected Boolean isResultFilesSame() {
        return false;
    }
}

