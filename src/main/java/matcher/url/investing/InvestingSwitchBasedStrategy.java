package matcher.url.investing;

import matcher.url.IBuildStrategy;
import matcher.url.UrlBuilder;

public class InvestingSwitchBasedStrategy implements IBuildStrategy {

    private final BuilderSwitch builderSwitch;

    public InvestingSwitchBasedStrategy(BuilderSwitch builderSwitch){

        this.builderSwitch = builderSwitch;
    }
    @Override
    public UrlBuilder resolve(String countryCode, String prefix, String wsjCode) {
        return this.builderSwitch.getForSearch()
                ? new InvestingSearchUrlBuilder()
                : new EquityPageUrlBuilder();
    }
}

