package matcher.url;

public final class DefaultUrlBuilder extends WSJUrlBuilder {

    @Override
    public String firstPart(String countryCode, String prefix, String wsjCode) {
        return countryCode + "/" + prefix + "/" + wsjCode;
    }
}
