package crawlee.org.src.common.mapper;


import crawlee.org.src.common.model.export.CsvExportDocument;
import crawlee.org.src.crawler.model.ScrapedDocument;

public interface ScrapedDocumentMapper {

    CsvExportDocument scrapedDocumentToCsvExportDocument(ScrapedDocument scrapedDocument);

}
