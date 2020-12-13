package matcher.url.wsj.impl;

import matcher.url.IBuildStrategy;
import matcher.url.UrlBuilder;

public class CountryCodeIsEmptyBuildStrategy implements IBuildStrategy {

    private final UrlBuilder notNull;
    private final UrlBuilder isNull;
    private final String emptyCriteria;

    public CountryCodeIsEmptyBuildStrategy(UrlBuilder notEmpty, UrlBuilder isEmpty, String emptyCriteria){

        this.notNull = notEmpty;
        this.isNull = isEmpty;
        this.emptyCriteria = emptyCriteria;
    }

    public CountryCodeIsEmptyBuildStrategy(String emptyCriteria){
        this(new DefaultUrlBuilder(), new USUrlBuilder(), emptyCriteria);
    }

    @Override
    public UrlBuilder resolve(String countryCode, String prefix, String wsjCode) {
        return countryCode == this.emptyCriteria ? isNull : notNull;
    }
}
