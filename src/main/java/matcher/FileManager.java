package matcher;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileManager {

    private final String wsjFileFolder;
    private final String wsjFileSuffix;
    private final String resultFileFolder;
    private final String resultFileSuffix;

    public FileManager(Configurationable config){
        this(config.getWSJFileFolder(), "list", config.getResultFileFolder(), config.getResultFileSuffix());
    }

    public FileManager(String wsjFileFolder, String wsjFileSuffix, String resultFileFolder, String resultFileSuffix) {

        this.wsjFileFolder = wsjFileFolder;
        this.wsjFileSuffix = wsjFileSuffix;
        this.resultFileFolder = resultFileFolder;
        this.resultFileSuffix = resultFileSuffix;
    }

    private List<String> getFolderFilesBySuffix(String folder, String suffix){
        return Arrays.stream(Objects.requireNonNull(new File(folder).listFiles()))
                .filter(file -> file.isFile() && file.getName().contains(suffix + ".xlsx"))
                .map(file -> folder + "/" + file.getName())
                .collect(Collectors.toList());
    }
    public List<String> getWSJFiles(){
        return this.getFolderFilesBySuffix(this.wsjFileFolder, this.wsjFileSuffix);
    }

    public List<String> getResultFiles(){
        return this.getFolderFilesBySuffix(this.resultFileFolder, this.resultFileSuffix);
    }

    public String getCountryByFileName(String filename){
        return new File(filename).getName().replace(wsjFileSuffix + ".xlsx", "");
    }
}
