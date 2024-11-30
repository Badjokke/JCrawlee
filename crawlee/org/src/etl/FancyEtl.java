package org.src.etl;


import org.src.etl.model.RestEtlConfig;

public class FancyEtl {
    public static void main(String[] args) {
        RestEtlConfig[] yitEtlConfigurations = ConfigLoader.loadEtlConfig("etl_config.json");
        AbstractRestEtl yit = new YitRestEtl(yitEtlConfigurations[0]);
        yit.extractAndSaveTransformedData();
    }
}
