package org.src.common.mapper.impl;

import org.src.common.mapper.ScrapedDocumentMapper;
import org.src.crawler.model.ScrapedDocument;
import org.src.common.model.export.CsvExportDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScrapedDocumentMapperImpl implements ScrapedDocumentMapper {

    private final String outputFilePrefix;

    public ScrapedDocumentMapperImpl(String outputFilePrefix) {
        this.outputFilePrefix = outputFilePrefix;
    }

    private int getRowCount(Map<String, List<String>> content) {
        Set<String> columns = content.keySet();
        int rowCount = 0;
        for (String column : columns) {
            List<String> columnValues = content.get(column);
            rowCount = Math.max(columnValues.size(), rowCount);
        }
        return rowCount;
    }


    @Override
    public CsvExportDocument scrapedDocumentToCsvExportDocument(ScrapedDocument scrapedDocument) {
        Set<String> columns = scrapedDocument.getContent().keySet();
        Map<String, List<String>> content = scrapedDocument.getContent();
        int rowCount = getRowCount(content);
        List<String[]> csvRows = new ArrayList<>(rowCount);
        for (int i = 0; i < rowCount; i++) {
            csvRows.add(new String[columns.size()]);
        }

        int walker = 0;
        for (String column : columns) {
            List<String> columnValues = content.get(column);
            for (int i = 0; i < columnValues.size(); i++) {
                csvRows.get(i)[walker] = columnValues.get(i);
            }
            walker++;
        }
        return new CsvExportDocument(outputFilePrefix + Math.random(), columns, csvRows);
    }
}
