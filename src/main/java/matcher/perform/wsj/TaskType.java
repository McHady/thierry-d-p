package matcher.perform.wsj;

public enum TaskType {
    LAST_PRICE("3-result", new LastPricePerformer()),
    OUTSTANDING_FLOAT("1", new SharesOutstandingPublicFloatPerformer());

    private final String suffix;
    private final WSJPerformer<?> performer;


    TaskType(String suffix, WSJPerformer<?> performer){
        this.suffix = suffix;
        this.performer = performer;
    }

    public static TaskType getTypeBySuffix(String suffix){
        for (var value: TaskType.values()) {

            if (value.suffix.equals(suffix))
                return value;
        }
        return null;
    }

    public WSJPerformer<?> getPerformer() { return this.performer; }
}

