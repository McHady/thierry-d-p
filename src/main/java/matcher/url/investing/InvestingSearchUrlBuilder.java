package matcher.url.investing;

import matcher.url.UrlBuilder;

public class InvestingSearchUrlBuilder extends UrlBuilder {

    @Override
    public String secondPart(String countryCode, String prefix, String wsjCode) {
        return "";
    }

    @Override
    public String firstPart(String countryCode, String prefix, String wsjCode) {
        return "search/?q=" + countryCode;
    }
}
