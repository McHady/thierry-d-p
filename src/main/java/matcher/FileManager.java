package matcher;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileManager {

    private String inputFileFolder;
    private String inputFileSuffix;
    private String resultFileFolder;
    private String resultFileSuffix;
    private Boolean ignoreFileNameParts = false;

    public FileManager(Configurationable config){
        this(config.getInputFileFolder(), "list", config.getResultFileFolder(), config.getResultFileSuffix());
    }

    public FileManager(String inputFileFolder, String inputFileSuffix, String resultFileFolder, String resultFileSuffix) {

        this.inputFileFolder = inputFileFolder;
        this.inputFileSuffix = inputFileSuffix;
        this.resultFileFolder = resultFileFolder;
        this.resultFileSuffix = resultFileSuffix;
    }

    public FileManager(Configurationable config, Boolean ignoreFileNameParts){

        this(config);
        this.ignoreFileNameParts = ignoreFileNameParts;
    }

    private List<String> getFolderFilesBySuffix(String folder, String suffix){
        return Arrays.stream(Objects.requireNonNull(new File(folder).listFiles()))
                .filter(file -> file.isFile() && file.getName().contains(suffix + ".xlsx"))
                .map(file -> folder + "/" + file.getName())
                .collect(Collectors.toList());
    }
    public List<String> getInputFiles(){
        return this.ignoreFileNameParts
                ? this.GetFolderFiles(this.inputFileFolder)
                : this.getFolderFilesBySuffix(this.inputFileFolder, this.inputFileSuffix);
    }

    private List<String> GetFolderFiles(String folder) {
        return Arrays.stream(Objects.requireNonNull(new File(folder).listFiles()))
                .filter(file -> file.isFile() && file.getName().contains(".xlsx"))
                .map(file -> folder + "/" + file.getName())
                .collect(Collectors.toList());
    }

    public List<String> getResultFiles(){
        return this.getFolderFilesBySuffix(this.resultFileFolder, this.resultFileSuffix);
    }

    public String getCountryByFileName(String filename){
        return new File(filename).getName().replace(inputFileSuffix + ".xlsx", "");
    }
}
