package matcher.url.impl;

import matcher.url.WSJUrlBuilder;

public final class USUrlBuilder extends WSJUrlBuilder {

    @Override
    public String firstPart(String countryCode, String prefix, String wsjCode) {
        return wsjCode;
    }

    @Override
    public String secondPart(String countryCode, String prefix, String wsjCode) {
        return super.secondPart("US", "", wsjCode);
    }
}
