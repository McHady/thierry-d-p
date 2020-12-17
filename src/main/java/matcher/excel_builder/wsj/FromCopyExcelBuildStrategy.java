package matcher.excel_builder.wsj;

import matcher.ElectronicDocument;
import matcher.ExcelManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FromCopyExcelBuildStrategy extends ExcelBuildStrategy {

    @Override
    public ExcelManager build(String destinationFileName, ElectronicDocument originalDocument, String country) {

        var originalFile = new File(originalDocument.getFileName());
        var destinationFile = new File(destinationFileName);
        try {
            Files.copy(originalFile.toPath(), destinationFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return new ExcelManager(destinationFileName);
    }
}

