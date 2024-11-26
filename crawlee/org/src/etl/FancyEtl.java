package org.src.etl;

import org.src.etl.model.YitEtlConfig;

public class FancyEtl {
    public static void main(String[] args) {
        YitEtlConfig[] yitEtlConfigurations = ConfigLoader.loadEtlConfig("etl_config.json");
        System.out.println("hello world");
    }
}
