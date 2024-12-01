package crawlee.org.src.common.constants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Constants {

    private Constants() {
    }

    public static final String TRAVERSAL_XPATH_KEYWORD = "traverse_expression";

    public static final String STORAGE_ROOT = "./storage";
    public static final Set<String> X_PATH_KEYWORDS = new HashSet<>(List.of(TRAVERSAL_XPATH_KEYWORD));
    public static final String CRAWLER_CONFIG_PATH = "crawlee/org/resources/crawler_config.json";
    public static final String ETL_CONFIG_PATH = "crawlee/org/resources/etl_config.json";

}