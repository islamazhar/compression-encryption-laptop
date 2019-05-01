package utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Files {
    public String folderName = null;
    public Files( String _folderName) {
        folderName = _folderName;
    }
    public List<String> getFiles() {
        List<String> folderFilesList = new ArrayList<>();
        File[] files = new File(folderName).listFiles();
        for(File file: files) {
            if(file.isFile()) {
                if(file.getName().charAt(0)=='.') continue;
                folderFilesList.add(file.getName());
            }
        }
        return folderFilesList;
    }
}
