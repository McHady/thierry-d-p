package matcher.script;

import matcher.Configurationable;
import matcher.FileManager;
import matcher.url.UrlGenerator;

import java.util.List;
import java.util.concurrent.Executors;

public abstract class InputFileListAsyncHandlingScript extends Script {

    public InputFileListAsyncHandlingScript(Configurationable config) {
        super(config);
    }

    @Override
    protected void process(UrlGenerator urlGenerator, FileManager fileManager) {

        var inputFiles = fileManager.getInputFiles();
        var resultFilesAreSame = this.isResultFilesSame();
        List<String> resultFiles = null;
        if (!resultFilesAreSame)
            resultFiles = fileManager.getResultFiles();

        var service = Executors.newFixedThreadPool(inputFiles.size());
        for (var inputFile : inputFiles) {

            List<String> finalResultFiles = resultFiles;
            var future = service.submit(() -> {
                if (finalResultFiles == null){
                    this.cycleIteration(fileManager, urlGenerator, inputFile);
                }
                else {
                    this.cycleIteration(fileManager, urlGenerator, inputFile, finalResultFiles);
                }
            });
        }

        service.shutdown();
    }

    protected abstract void cycleIteration(FileManager fileManager, UrlGenerator urlGenerator, String inputFile);

    protected abstract void cycleIteration(FileManager fileManager, UrlGenerator urlGenerator, String inputFile, List<String> finalResultFiles);

    protected abstract Boolean isResultFilesSame();
}
