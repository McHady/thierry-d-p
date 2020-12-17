package matcher;

public interface Configurationable {
    String getWSJCountryCodeColumn();

    String getWSJPrefixColumn();

    String getWSJCodeColumn();

    String getWSJKeyColumn();

    String getResultFileFolder();

    String getInputFileFolder();
    String getInputFileSuffix();

    String getResultFileSuffix();

    int getScriptCode();
    Integer getInvestingDelay();
}
