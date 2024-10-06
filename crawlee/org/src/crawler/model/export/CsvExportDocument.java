package org.src.crawler.model.export;

import java.util.List;
import java.util.Set;

public class CsvExportDocument extends ExportDocument{
    private final Set<String> columns;
    private final List<String[]> content;

    public CsvExportDocument(String filename, Set<String> columns, List<String[]> content){
        this.fileExtension = "csv";
        this.filename = filename;
        this.content = content;
        this.columns = columns;
    }





}
