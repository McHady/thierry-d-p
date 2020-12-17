package matcher.excel_builder.wsj;

import matcher.ElectronicDocument;
import matcher.ExcelManager;

public abstract class ExcelBuildStrategy {
    public abstract ExcelManager build(String destinationFileName, ElectronicDocument originalDocument, String country);
}
