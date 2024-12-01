package crawlee.org.src.common.model.export;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CsvExportDocument extends ExportDocument {
    private final Set<String> columns;
    private final List<String[]> content;

    public CsvExportDocument(String filename, Set<String> columns, List<String[]> content) {
        this.fileExtension = "csv";
        this.filename = filename;
        this.content = content;
        this.columns = columns;
    }


    @Override
    public byte[] getContent() {
        String csvFile = createCSVHeader() + "\n" + createCsvRows();
        return csvFile.getBytes();
    }

    private String createCSVHeader() {
        return valuesToCsvLine(columns);
    }

    private String createCsvRows() {
        StringBuilder rows = new StringBuilder();
        for (String[] arr : content) {
            rows.append(String.join(";", arr)).append("\n");
        }
        return rows.toString();
    }

    private <T> String valuesToCsvLine(Collection<T> values) {
        StringBuilder builder = new StringBuilder();
        Iterator<T> iterator = values.iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next());
            if (!iterator.hasNext()) {
                break;
            }
            builder.append(",");
        }
        return builder.toString();
    }

}
