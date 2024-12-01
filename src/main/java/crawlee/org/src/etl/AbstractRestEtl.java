package crawlee.org.src.etl;


import crawlee.org.src.etl.model.RestEtlConfig;

public abstract class AbstractRestEtl {
    private final RestEtlConfig restEtlConfig;

    public AbstractRestEtl(RestEtlConfig restEtlConfig) {
        this.restEtlConfig = restEtlConfig;
    }

    protected RestEtlConfig getRestEtlConfig() {
        return this.restEtlConfig;
    }

    public abstract void extractAndSaveTransformedData();

}
