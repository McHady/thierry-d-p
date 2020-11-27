package matcher.url.impl;

import matcher.url.WSJUrlBuilder;

public final class DefaultUrlBuilder extends WSJUrlBuilder {

    @Override
    public String firstPart(String countryCode, String prefix, String wsjCode) {
        return countryCode + "/" + prefix + "/" + wsjCode;
    }
}
