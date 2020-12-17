package matcher.script;

import matcher.Configurationable;
import matcher.ExcelManager;
import matcher.FileManager;
import matcher.url.UrlGenerator;
import matcher.url.investing.BuilderSwitch;
import matcher.url.investing.InvestingSwitchBasedStrategy;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class InvestingScript extends Script {

    private final String urlRoot = "https://www.investing.com/";
    public InvestingScript(Configurationable config) {
        super(config);
    }

    @Override
    protected void process(UrlGenerator urlGenerator, FileManager fileManager) {

        var inputFiles = fileManager.getInputFiles();
        var builderSwitch = new BuilderSwitch();
        urlGenerator = new UrlGenerator(new InvestingSwitchBasedStrategy(builderSwitch), this.urlRoot);

        for (var inputFile : inputFiles){
            try (var excel = new ExcelManager(inputFile)) {

                var rowNumber = excel.getRowNumber();
                for(var i=1; i < rowNumber; i++){

                    var investingCode = excel.getCellValue(i, "AQ");
                    if (excel.getCellValue(i, "A") == null || investingCode == null || excel.getCellValue(i, "AF") != null)
                        continue;

                    var stockName = excel.getCellValue(i, "AR");

                    builderSwitch.setForSearch(true);
                    var searchUrl = urlGenerator.generate(investingCode, null, null);
                    System.out.println(searchUrl);

                    var searchHtml = Jsoup.connect(searchUrl).ignoreContentType(true).get();
                    var searchResultTableElements = searchHtml.select("#fullColumn > div > div.js-section-wrapper.searchSection.allSection > div:nth-child(2) > div.js-inner-all-results-quotes-wrapper.newResultsContainer.quatesTable > a");

                    UrlGenerator finalUrlGenerator = urlGenerator;
                    int finalI = i;
                    searchResultTableElements.forEach(element -> {

                        AtomicBoolean hasRequiredStockName = new AtomicBoolean(false);
                        element.children().forEach(c -> {

                            if (c.hasText() && c.text().contains(stockName)){
                                hasRequiredStockName.set(true);
                            }
                        });

                        if (hasRequiredStockName.get() && element.hasAttr("href")){

                            var href = element.attr("href");

                            if (href != null && href.contains("equities")) {
                                builderSwitch.setForSearch(false);
                                var equityUrl = finalUrlGenerator.generate(href, null, null);
                                System.out.println(equityUrl);

                                try {
                                    var equityHtml = Jsoup.connect(equityUrl).ignoreContentType(true).get();
                                    if (equityHtml != null){
                                        var sharedOutstandingElement = equityHtml.select("#leftColumn > div.clear.overviewDataTable.overviewDataTableWithTooltip > div:nth-child(14)");
                                        var sharedOutstanding = sharedOutstandingElement.last().children().last().text().replace(",", "");
                                        System.out.println(sharedOutstanding);
                                        if (!sharedOutstanding.isBlank()){
                                            excel.setCellValue(finalI, "AF",  sharedOutstanding);
                                            excel.update();
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    try {
                        Thread.sleep(this.config.getInvestingDelay() * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected Boolean ignoreFileNameParts() {
        return true;
    }

    @Override
    protected UrlGenerator getUrlGenerator() {
        return null;
    }
}
