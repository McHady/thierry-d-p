package matcher.url.wsj.impl;

import matcher.url.wsj.WSJUrlBuilder;

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
