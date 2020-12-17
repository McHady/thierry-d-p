package matcher;

import java.io.*;
import java.util.Properties;

public class Configuration implements Configurationable {

    private Integer investingDelay;
    private Integer scriptCode;
    private String resultFileFolder = ".";

    private String inputFileSuffix = "3";
    private String inputFileFolder = ".";
    private String wsjCountryCodeColumn = "AH";
    private String wsjPrefixColumn = "AI";
    private String wsjCodeColumn = "D";
    private String wsjKeyColumn = "B";

    private String resultFileSuffix = "3-result";

    public Configuration(String filename) {



        try (var stream = new FileInputStream(new File((filename)))){

            var properties = new Properties();
            properties.load(stream);

            this.scriptCode = Integer.parseInt( properties.getProperty("SCRIPT_CODE", "0") );
            this.investingDelay = Integer.parseInt( properties.getProperty("INVESTING_REQUEST_DELAY", "13"));

            this.inputFileFolder = properties.getProperty("INPUT_FILE_FOLDER", ".");
            this.inputFileSuffix = properties.getProperty("INPUT_FILE_SUFFIX", "3");
            this.wsjCountryCodeColumn = properties.getProperty("WSJ_COUNTRY_CODE_COLUMN", "AH");
            this.wsjPrefixColumn = properties.getProperty("WSJ_PREFIX_COLUMN", "AI");
            this.wsjCodeColumn = properties.getProperty("WSJ_CODE_COLUMN", "D");
            this.wsjKeyColumn = properties.getProperty("WSJ_KEY_COLUMN", "B");

            this.resultFileFolder = properties.getProperty("RESULT_FILE_FOLDER", ".");
            this.resultFileSuffix = properties.getProperty("RESULT_FILE_SUFFIX", "3-result");

        } catch (FileNotFoundException e) {
            System.out.println("Can't find configuration file");
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

    public String getWSJKeyColumn() {
        return wsjKeyColumn;
    }

    public String getResultFileFolder() {
        return resultFileFolder;
    }

    public String getInputFileFolder() {
        return inputFileFolder;
    }

    @Override
    public String getInputFileSuffix() {
        return this.inputFileSuffix;
    }

    @Override
    public String getResultFileSuffix() {
        return this.resultFileSuffix;
    }

    @Override
    public int getScriptCode() {
        return this.scriptCode;
    }

    public Integer getInvestingDelay() {
        return investingDelay;
    }
}

