package matcher;

import java.io.*;
import java.util.Properties;

public class Configuration {

    private String resultTableFile = "Austria 3-result.xlsx";
    private String resultDestinationColumn = "M";
    private String resultKeyColumn = "B";

    private String wsjTableFile = "Austria list.xlsx";
    private String wsjCountryCodeColumn = "AH";
    private String wsjPrefixColumn = "AI";
    private String wsjCodeColumn = "D";
    private String wsjKeyColumn = "B";

    private String urlRoot = "https://www.wsj.com/market-data/quotes/";

    public Configuration(String filename) {



        try (var stream = new FileInputStream(new File((filename)))){

            var properties = new Properties();
            properties.load(stream);

            this.wsjTableFile = properties.getProperty("WSJ_TABLE_FILE", "DefaultWSJ.xlsx");
            this.wsjCountryCodeColumn = properties.getProperty("WSJ_COUNTRY_CODE_COLUMN", "AH");
            this.wsjPrefixColumn = properties.getProperty("WSJ_PREFIX_COLUMN", "AI");
            this.wsjCodeColumn = properties.getProperty("WSJ_CODE_COLUMN", "D");
            this.wsjKeyColumn = properties.getProperty("WSJ_KEY_COLUMN", "B");

            this.resultTableFile = properties.getProperty("RESULT_TABLE_FILE", "Result.xlsx");
            this.resultDestinationColumn = properties.getProperty("RESULT_DESTINATION_COLUMN", "M");
            this.resultKeyColumn = properties.getProperty("RESULT_KEY_COLUMN", "B");

            this.urlRoot = properties.getProperty("URL_ROOT", "https://www.wsj.com/market-data/quotes/");
            if (!this.urlRoot.endsWith("/"))
                this.urlRoot += "/";

        } catch (FileNotFoundException e) {
            System.out.println("Can't find configuration file");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getWSJCountryCodeColumn() {
        return this.wsjCountryCodeColumn;
    }

    public String getWSJPrefixColumn() {
        return this.wsjPrefixColumn;
    }

    public String getWSJCodeColumn() {
        return this.wsjCodeColumn;
    }

    public String getUrlRoot() {
        return this.urlRoot;
    }

    public String getWSJTableFile() {
        return wsjTableFile;
    }

    public String getResultTableFile() {
        return resultTableFile;
    }

    public String getWSJKeyColumn() {
        return wsjKeyColumn;
    }

    public String getResultDestinationColumn() {
        return resultDestinationColumn;
    }

    public String getResultKeyColumn() {
        return resultKeyColumn;
    }
}
