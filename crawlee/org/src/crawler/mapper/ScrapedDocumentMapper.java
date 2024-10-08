package org.src.crawler.mapper;

import org.src.crawler.model.ScrapedDocument;
import org.src.crawler.model.export.CsvExportDocument;

public interface ScrapedDocumentMapper {

    CsvExportDocument scrapedDocumentToCsvExportDocument(ScrapedDocument scrapedDocument);

}
