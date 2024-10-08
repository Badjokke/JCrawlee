package org.src.crawler.io;

import org.src.crawler.constants.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class IOManager {
    private static final Logger log = Logger.getLogger(IOManager.class.getSimpleName());

    public IOManager() {
        //no instances
    }

    /**
     * @param rewrite if this flag is set to true, file will be rewritten
     * @return true if file was created
     */
    public static boolean createDocumentStorage(boolean rewrite) {
        log.info("Creating document storage2");
        try {
            return createFile(rewrite, Constants.storageRoot);
        } catch (IOException exception) {
            log.warning(String.format("Failed to create document storage: %s", exception.getMessage()));
            exception.printStackTrace();
            return false;
        }
    }

    private static boolean createFile(boolean rewrite, String path) throws IOException {
        File rootStorage = new File(path);
        log.info(String.format("Creating file: %s\n Override: %s", rootStorage.getAbsoluteFile(), rewrite));
        if (!rootStorage.exists() || rewrite) {
            if (!rootStorage.mkdir()) {
                log.warning(String.format("mkdir with arg: %s failed", rootStorage.getPath()));
                return false;
            }
        }
        return true;
    }

    public static boolean writeFile(String path, byte[] content){
        File file = new File(Constants.storageRoot+"/"+path);
        if(file.exists()){
            log.fine(String.format("File: %s already exists and will be overwritten.", path));
        }
        try(FileOutputStream outputStream = new FileOutputStream(file,false)){
            outputStream.write(content);
        }
        catch (IOException e){
            log.warning(String.format("Unable to write file: %s.\n Reason: %s", file.getAbsoluteFile(), e.getMessage()));
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
