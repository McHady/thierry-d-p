package matcher.excel_builder.wsj;

import matcher.ElectronicDocument;
import matcher.ExcelManager;

import java.io.File;

public class FromAnotherFileCopyExelBuildStrategy extends FromCopyExcelBuildStrategy {
    private final String suffix;

    public FromAnotherFileCopyExelBuildStrategy(String suffix) {

        this.suffix = suffix;
    }

    @Override
    public ExcelManager build(String destinationFileName, ElectronicDocument originalDocument, String country) {

        var listFile = new File( originalDocument.getFileName() );
        return super.build(destinationFileName, () -> new File( listFile.toPath().getParent().toString(), country + " " + this.suffix + ".xlsx" ).getPath(), country);
    }
}
