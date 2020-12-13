package matcher;

public interface Configurationable {
    String getWSJCountryCodeColumn();

    String getWSJPrefixColumn();

    String getWSJCodeColumn();

    String getUrlRoot();

    String getWSJKeyColumn();

    String getResultFileFolder();

    String getInputFileFolder();

    String getResultFileSuffix();

    int getScriptCode();
    Integer getInvestingDelay();
}
