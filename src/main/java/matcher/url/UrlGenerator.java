package matcher.url;

import matcher.url.impl.CountryCodeIsEmptyBuildStrategy;

public class UrlGenerator {

    private final IBuildStrategy buildStrategy;
    private final String urlRoot;

    public UrlGenerator(IBuildStrategy buildStrategy, String urlRoot){

        this.buildStrategy = buildStrategy;
        this.urlRoot = urlRoot;
    }

    public UrlGenerator(String urlRoot, String emptyCriteria) {

        this(new CountryCodeIsEmptyBuildStrategy(emptyCriteria), urlRoot);
    }

    public  UrlGenerator(String urlRoot) {
        this(urlRoot, null);
    }

    public String generate(String countryCode, String prefix, String wsjCode){

        var builder = this.buildStrategy.resolve(countryCode, prefix, wsjCode);

        return this.urlRoot + builder.firstPart(countryCode, prefix, wsjCode) + builder.secondPart(countryCode, prefix, wsjCode);
    }
}
