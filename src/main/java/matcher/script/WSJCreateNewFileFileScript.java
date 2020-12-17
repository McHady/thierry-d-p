package matcher.script;

import matcher.Configurationable;
import matcher.ElectronicDocument;
import matcher.ExcelManager;
import matcher.FileManager;
import matcher.excel_builder.wsj.ExcelBuilder;

public class WSJCreateNewFileFileScript extends WSJScript {
    public WSJCreateNewFileFileScript(Configurationable config) {
        super(config);
    }

    @Override
    protected ExcelManager buildResultExcelFile(String fileName, ElectronicDocument originFile, String country) throws NullPointerException {

        try {
            return super.buildResultExcelFile(fileName, originFile, country);
        }
        catch (NullPointerException e) {

            System.out.println("Couldn't find result file for country: " + country + ", so creating a new one");

            var resultExcel = ExcelBuilder.build(this.config, originFile, country);

            if (resultExcel != null)
                return resultExcel;
            else
                throw new NullPointerException("Can't build file " + fileName);
        }
    }
}
