package matcher.perform.wsj;

import matcher.excel_builder.wsj.ExcelBuildStrategy;
import matcher.excel_builder.wsj.FromAnotherFileCopyExelBuildStrategy;
import matcher.excel_builder.wsj.FromCopyExcelBuildStrategy;

public enum TaskType {
    LAST_PRICE("3-result", new LastPricePerformer(), new FromAnotherFileCopyExelBuildStrategy("3")),
    OUTSTANDING_FLOAT("1", new SharesOutstandingPublicFloatPerformer(), null);

    private final String suffix;
    private final WSJPerformer<?> performer;
    private final ExcelBuildStrategy buildStrategy;


    TaskType(String suffix, WSJPerformer<?> performer, ExcelBuildStrategy buildStrategy){
        this.suffix = suffix;
        this.performer = performer;
        this.buildStrategy = buildStrategy;
    }

    public static TaskType getTypeBySuffix(String suffix){
        for (var value: TaskType.values()) {

            if (value.suffix.equals(suffix))
                return value;
        }
        return null;
    }

    public WSJPerformer<?> getPerformer() { return this.performer; }

    public ExcelBuildStrategy getBuildStrategy() {
        return buildStrategy;
    }
}

