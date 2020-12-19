package matcher;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileManager {

    private String inputFileFolder;
    private String inputFileSuffix;
    private final String inputFileExtension;
    private String resultFileFolder;
    private String resultFileSuffix;
    private final String resultFileExtension;
    private Boolean ignoreFileNameParts = false;

    public FileManager(Configurationable config){
        this(config.getInputFileFolder(), config.getInputFileSuffix(), config.getResultFileFolder(), config.getResultFileSuffix());
    }

    public FileManager(String inputFileFolder, String inputFileSuffix, String inputFileExtension, String resultFileFolder, String resultFileSuffix, String resultFileExtension)
    {
        this.inputFileFolder = inputFileFolder;
        this.inputFileSuffix = inputFileSuffix;
        this.inputFileExtension = inputFileExtension;
        this.resultFileFolder = resultFileFolder;
        this.resultFileSuffix = resultFileSuffix;
        this.resultFileExtension = resultFileExtension;
    }
    public FileManager(String inputFileFolder, String inputFileSuffix, String resultFileFolder, String resultFileSuffix) {

        this(inputFileFolder, inputFileSuffix, "xlsx", resultFileFolder, resultFileSuffix, "xlsx");
    }

    public FileManager(Configurationable config, Boolean ignoreFileNameParts){

        this(config);
        this.ignoreFileNameParts = ignoreFileNameParts;
    }

    private List<String> getFolderFilesBySuffix(String folder, String suffix, String extension){
        return Arrays.stream(Objects.requireNonNull(new File(folder).listFiles()))
                .filter(file -> file.isFile() && file.getName().contains(suffix + "." + extension))
                .map(file -> folder + "/" + file.getName())
                .collect(Collectors.toList());
    }
    public List<String> getInputFiles(){
        return this.ignoreFileNameParts
                ? this.GetFolderFiles(this.inputFileFolder, this.inputFileExtension)
                : this.getFolderFilesBySuffix(this.inputFileFolder, this.inputFileSuffix, this.inputFileExtension);
    }

    private List<String> GetFolderFiles(String folder, String extension) {
        return Arrays.stream(Objects.requireNonNull(new File(folder).listFiles()))
                .filter(file -> file.isFile() && file.getName().contains("." + extension))
                .map(file -> folder + "/" + file.getName())
                .collect(Collectors.toList());
    }

    public List<String> getResultFiles(){
        return this.getFolderFilesBySuffix(this.resultFileFolder, this.resultFileSuffix, this.resultFileExtension);
    }

    public String getCountryByFileName(String filename){
        return new File(filename).getName().replace(inputFileSuffix + "." + inputFileExtension, "").trim();
    }
}
