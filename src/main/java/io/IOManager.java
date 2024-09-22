package io;

import constants.Constants;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

//class responsible for I/O operations
public class IOManager {
    private static final JSONParser jsonParser = new JSONParser();

    /**
     * create storage files if they dont exist
     *
     * @param rewrite if this parameter is set to true, files will be rewritten even if they exist
     */
    public static boolean createStorage(boolean rewrite) {
        boolean fileCreated = false;

        try {
            fileCreated = createFile(rewrite, Constants.storageRoot);

        } catch (IOException exception) {
            exception.printStackTrace();
            return false;

        }
        return fileCreated;
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

    public static JSONObject readJSONfile(String path) {
        BufferedReader br = null;
        JSONObject jsonObject = null;
        try {
            br = new BufferedReader(new FileReader(path));
            jsonObject = (JSONObject) jsonParser.parse(br);
            br.close();

        } catch (ParseException | IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;

    }

}
