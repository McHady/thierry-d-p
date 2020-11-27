package matcher.url;

public abstract class UrlBuilder {
    public abstract String secondPart(String countryCode, String prefix, String wsjCode);

    public abstract String firstPart(String countryCode, String prefix, String wsjCode);
}

