package org.src.common.mapper;

import org.src.crawler.model.ScrapedDocument;
import org.src.common.model.export.CsvExportDocument;

public interface ScrapedDocumentMapper {

    CsvExportDocument scrapedDocumentToCsvExportDocument(ScrapedDocument scrapedDocument);

}
