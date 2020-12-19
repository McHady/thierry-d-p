package matcher.script;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import matcher.Configurationable;
import matcher.FileManager;
import matcher.url.UrlGenerator;
import org.jsoup.Jsoup;

import java.io.*;
import java.util.List;
import java.util.Locale;

public class YahooScript extends InputFileListAsyncHandlingScript {

    public YahooScript(Configurationable config) {
        super(config);
    }

    @Override
    protected void cycleIteration(FileManager fileManager, UrlGenerator urlGenerator, String inputFile) {

        try(var reader = new CSVReader(new FileReader(inputFile), ';', '"', 0)){

            var country = fileManager.getCountryByFileName(inputFile);
            System.out.println("Starting file for country " + country);
            var outputFileName = new File(this.config.getResultFileFolder(), country + " " + this.config.getResultFileSuffix()  + ".csv").getPath();

            {

                String[] nextLine;
                var lineIndex = 0;
                while ((nextLine = reader.readNext()) != null) {

                    var fileWriter = new FileWriter(outputFileName, lineIndex != 0);

                    try (var writer = new CSVWriter(fileWriter, ';', '"')) {
                        if (lineIndex > 0) {

                            var code = nextLine[2];
                            System.out.printf("Country: %s, Line: %d, code: %s%n", country, lineIndex, code);
                            var url = buildUrl(code);
                            var value = getValue(url);

                            if (value != null)
                                nextLine[11] = value.toString().replace(".", ",");
                        }
                        writer.writeNext(nextLine);
                    }
                    lineIndex++;
                }
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Double getValue(String url) {

        try {
            var responseString = Jsoup.connect(url).ignoreContentType(true).get().text();
            var json = (JsonObject) new JsonParser().parse((responseString));
            var priceElement = json.getAsJsonObject("chart")
                    .getAsJsonArray("result")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("meta")
                    .get("regularMarketPrice");
            return priceElement.isJsonNull() ? null : priceElement.getAsDouble();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String buildUrl(String code) {

        return "https://query1.finance.yahoo.com/v8/finance/chart/" + code +
                "?region=US&lang=en-US&includePrePost=false&interval=2m&range=1d&corsDomain=finance.yahoo.com&.tsrc=finance";
    }

    @Override
    protected void cycleIteration(FileManager fileManager, UrlGenerator urlGenerator, String inputFile, List<String> finalResultFiles) {

    }

    @Override
    protected Boolean isResultFilesSame() {
        return true;
    }

    @Override
    protected Boolean ignoreFileNameParts() {
        return false;
    }

    @Override
    protected UrlGenerator getUrlGenerator() {
        return null;
    }

    @Override
    protected FileManager buildFileManager() {
        return new FileManager(config.getInputFileFolder(), "3", "csv", config.getResultFileFolder(), "3-result", "csv");
    }
}
