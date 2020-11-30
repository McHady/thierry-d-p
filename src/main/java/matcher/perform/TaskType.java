package matcher.perform;

public enum TaskType {
    LAST_PRICE("3-result", new LastPricePerformer()),
    OUTSTANDING_FLOAT("1", new SharesOutstandingPublicFloatPerformer());

    private final String suffix;
    private final Performer<?> performer;

    TaskType(String suffix, Performer<?> performer){

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

    public Performer<?> getPerformer() { return this.performer; }
}

