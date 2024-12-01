package org.src.etl;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.src.common.constants.Constants;
import org.src.etl.model.RestEtlConfig;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public final class ConfigLoader {
    private static final Logger log = Logger.getLogger(ConfigLoader.class.getCanonicalName());

    private ConfigLoader() {

    }

    public static RestEtlConfig[] loadEtlConfig(String path) {
        File f = new File(path);
        if (!f.exists()) {
            log.warning(String.format("Config file: %s not found\n", path));
            return null;
        }
        try (FileReader fReader = new FileReader(f)) {
            return new Gson().fromJson(new JsonReader(fReader), RestEtlConfig[].class);
        } catch (IOException e) {
            log.warning(String.format("Config file not found at: %s\n", path));
            throw new RuntimeException(e);
        }

    }
}
