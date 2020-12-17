package matcher.excel_builder.wsj;

import matcher.Configurationable;
import matcher.ElectronicDocument;
import matcher.ExcelManager;
import matcher.perform.wsj.TaskType;

import java.io.File;
import java.util.Objects;

public class ExcelBuilder {

    public static ExcelManager build(Configurationable config, ElectronicDocument originalDocument, String country) {
       return build(
                Objects.requireNonNull( TaskType.getTypeBySuffix(config.getResultFileSuffix()) ),
                config.getResultFileFolder(),
                country + " " + config.getResultFileSuffix() + ".xlsx",
                originalDocument,
                country);
    }

    public static ExcelManager build(TaskType taskType, String fileRoot, String fileName, ElectronicDocument originalDocument, String country) {
        return build(taskType, new File(fileRoot, fileName).getPath(), originalDocument, country);
    }

    public static ExcelManager build(TaskType taskType, String destinationFile, ElectronicDocument originalDocument, String country){
        return taskType.getBuildStrategy().build(destinationFile, originalDocument, country);
    }
}


