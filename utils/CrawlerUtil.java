import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class CrawlerUtil {
    //crawler configuration file
    private static JSONObject config;
    //maximum depth of crawling
    private static int crawlingDepth;
    //how long we sleep the thread before hitting the server again
    private static int politeness;
    //xpaths used for information extraction
    private static List<String> xPaths;
    //root page we want to crawl
    private static String rootPage;
    //subcategory we want to crawl, can be null
    private static String pageSubcategory;
    private static boolean isActive = false;
    /*public CrawlerUtil(){
        //initialize default values of crawler
        this.crawlingDepth = 1;
        this.politeness = 1000;
        this.xPaths = null;
        this.rootPage = Constants.rootUrl;
        this.pageSubcategory = null;

        config = IOManager.readJSONfile(Constants.CRAWLER_CONFIG_PATH);
        this.parseConfigFile();
    }*/

    public synchronized static void setIsActive(boolean active){
        isActive = active;
    }
    public synchronized static boolean isActive(){
        return isActive;
    }

    public static void loadConfigFile(){
        config = IOManager.readJSONfile(Constants.CRAWLER_CONFIG_PATH);
        if(config == null)return;
        config = (JSONObject) config.get("crawler");
        parseConfigFile();
    }

    private static void parseConfigFile(){
        if(config == null)return;
        Object tmp = null;
        tmp =  config.get("crawling_depth");

        if(tmp != null)
            crawlingDepth =  Integer.parseInt((String) tmp);
        tmp = config.get("politeness");

        if(tmp != null)
            politeness = Integer.parseInt((String) tmp);
        tmp = config.get("xpaths");

        if(tmp != null){
            xPaths = new ArrayList<>();
            Iterator arrayIterator = ((JSONArray) tmp).iterator();
            while(arrayIterator.hasNext()){
                xPaths.add((String) arrayIterator.next());
            }

        }
        tmp = config.get("root_page");
        if(tmp != null)
            rootPage = (String) tmp;

        tmp = config.get("page_subcategory");
        if(tmp != null)
            pageSubcategory = (String) tmp;

    }

    public static String getRootPage() {
        return rootPage;
    }

    public static String getPageSubcategory(){
        return pageSubcategory != null ? rootPage+"/"+pageSubcategory : rootPage;
    }
    public static List<String> getXPaths(){
        return xPaths;
    }
    public static int getCrawlingDepth(){
        return crawlingDepth;
    }
    public static int getPoliteness(){
        return politeness;
    }

}
