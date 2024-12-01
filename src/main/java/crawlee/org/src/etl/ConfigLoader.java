package crawlee.org.src.etl;

import com.google.gson.Gson;
import crawlee.org.src.etl.model.RestEtlConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public final class ConfigLoader {
    private static final Logger log = Logger.getLogger(ConfigLoader.class.getCanonicalName());

    private ConfigLoader() {

    }

    public static RestEtlConfig[] loadEtlConfig(String path) {
        try (InputStream url = ConfigLoader.class.getClassLoader().getResourceAsStream(path)) {
            assert url != null : "resource at: " + path + " is null";
            return new Gson().fromJson(new InputStreamReader(url), RestEtlConfig[].class);
        } catch (IOException e) {
            log.warning(String.format("Config file not found at: %s\n", path));
            throw new RuntimeException(e);
        }

    }
}
