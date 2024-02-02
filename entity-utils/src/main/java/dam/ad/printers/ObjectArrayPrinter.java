package dam.ad.printers;

import dam.ad.converters.ObjectArrayRowConverter;
import dam.ad.converters.RowConverter;
import dam.ad.headers.GenericHeaderProvider;
import dam.ad.headers.HeaderProvider;

public class ObjectArrayPrinter extends DefaultDTOPrinter<Object[]> {
    public ObjectArrayPrinter(HeaderProvider headerProvider, ObjectArrayRowConverter rowConverter) {
        super(headerProvider, rowConverter);
    }

    public ObjectArrayPrinter(String[] labels, int[] columnLengths) {
        this(new GenericHeaderProvider(labels, columnLengths), new ObjectArrayRowConverter(columnLengths));
    }

    public ObjectArrayPrinter(String label1, int columnLength1, String label2, int columnLength2) {
        this(new String[]{label1, label2}, new int[]{columnLength1, columnLength2});
    }

    public ObjectArrayPrinter(String label1, int columnLength1, String label2, int columnLength2, String label3, int columnLength3) {
        this(new String[]{label1, label2, label3}, new int[]{columnLength1, columnLength2, columnLength3});
    }
}
