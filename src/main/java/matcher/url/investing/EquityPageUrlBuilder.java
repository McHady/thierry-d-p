package matcher.url.investing;

import matcher.url.UrlBuilder;

public class EquityPageUrlBuilder extends UrlBuilder {

    @Override
    public String secondPart(String countryCode, String prefix, String wsjCode) {
        return countryCode.substring(1);
    }

    @Override
    public String firstPart(String countryCode, String prefix, String wsjCode) {
        return "";
    }
}
