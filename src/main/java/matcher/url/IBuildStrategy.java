package matcher.url;

public interface IBuildStrategy {

    UrlBuilder resolve(String countryCode, String prefix, String wsjCode);
}

