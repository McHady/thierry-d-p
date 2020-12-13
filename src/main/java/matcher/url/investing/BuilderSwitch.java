package matcher.url.investing;

public class BuilderSwitch {
    private Boolean isForSearch;

    public Boolean getForSearch() {
        return isForSearch;
    }

    public BuilderSwitch setForSearch(Boolean forSearch) {
        isForSearch = forSearch;
        return this;
    }
}
