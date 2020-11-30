package matcher;

public interface Configurationable {
    String getWSJCountryCodeColumn();

    String getWSJPrefixColumn();

    String getWSJCodeColumn();

    String getUrlRoot();

    String getWSJKeyColumn();

    String getResultFileFolder();

    String getWSJFileFolder();

    String getResultFileSuffix();
}
