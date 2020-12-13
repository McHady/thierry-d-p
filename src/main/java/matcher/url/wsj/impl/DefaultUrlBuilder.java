package matcher.url.wsj.impl;

import matcher.url.wsj.WSJUrlBuilder;

public final class DefaultUrlBuilder extends WSJUrlBuilder {

    @Override
    public String firstPart(String countryCode, String prefix, String wsjCode) {
        return countryCode + "/" + prefix + "/" + wsjCode;
    }
}
