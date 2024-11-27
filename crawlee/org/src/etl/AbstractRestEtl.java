package org.src.etl;

import org.src.common.model.export.ExportDocument;
import org.src.etl.model.RestEtlConfig;

public abstract class AbstractRestEtl {
    private final RestEtlConfig restEtlConfig;

    public AbstractRestEtl(RestEtlConfig restEtlConfig) {
        this.restEtlConfig = restEtlConfig;
    }

    protected RestEtlConfig getRestEtlConfig() {
        return this.restEtlConfig;
    }

    public abstract ExportDocument extractTransformLoad();

}
