package org.src.common.model.export;

public abstract class ExportDocument {

    protected String filename;
    protected String fileExtension;
    protected byte[] content;

    public String getFilename() {
        return filename;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public byte[] getContent() {
        return content;
    }


}
