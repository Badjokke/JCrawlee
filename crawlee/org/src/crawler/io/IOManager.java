package org.src.crawler.io;

import org.src.crawler.constants.Constants;
import java.io.*;

//class responsible for I/O operations
public class IOManager {

    public IOManager() {
        //no instances
    }

    /**
     * @param rewrite if this flag is set to true, file will be rewritten
     * @return true if file was created
     */
    public static boolean createDocumentStorage(boolean rewrite) {
        boolean fileCreated = false;
        try {
            fileCreated = createFile(rewrite, Constants.crawlerFileStorage);
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
        return fileCreated;
    }

    public static boolean writeJSONfile(String content, String path) throws IOException {
        //createFile(true,path);
        FileWriter file = new FileWriter(path);
        file.write(content);
        file.flush();
        file.close();
        return true;
    }


    private static boolean createFile(boolean rewrite, String path) throws IOException {
        File rootStorage = new File(path);
        boolean fileCreated = true;
        if (!rootStorage.exists() || rewrite) {
            //Global.logger.log(Level.INFO,"Creating file: "+path+" on filesystem.");
            fileCreated = rootStorage.mkdir();
            if (!fileCreated) {
                //Log.log(Level.SEVERE,"Failed to create file "+ path);
            }
        }
        return fileCreated;
    }


}
