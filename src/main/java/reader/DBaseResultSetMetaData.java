package reader;

import bean.DBaseHeader;

import java.io.Reader;

/**
 * Created by Hongzhe on 3/29/2017.
 */
public class DBaseResultSetMetaData {

    private DBaseHeader header;

    public DBaseResultSetMetaData(DBaseHeader headaer) {
        this.header = header;
    }

    public int getColumnCount() {
        return header.getDBaseFields().size();
    }

    public String getColumnName(int columnIndex) throws DBaseReaderException {
        ReaderUtil.validateIndex(columnIndex, header);
       return header.getDBaseFields().get(columnIndex).getName();
    }

    public int getPrecision(int columnIndex) throws DBaseReaderException {
        String type = getColumnType(columnIndex);
        int precision = 0;
        if (type.equals("N")) {
            precision = header.getDBaseFields().get(columnIndex).getDecimalcount();
        } else if (type.equals("C")) {
            precision =  header.getDBaseFields().get(columnIndex).getLength();
        } else {
            throw new DBaseReaderException(type + " datatype is not support");
        }
        return precision;
    }

    public String getColumnType(int columnIndex) throws DBaseReaderException {
        ReaderUtil.validateIndex(columnIndex, header);
        char type = header.getDBaseFields().get(columnIndex).getFieldtype();
        return type + "";
    }

    public String getColumnType(String columnName) throws DBaseReaderException {
        ReaderUtil.validateColumnName(columnName, header);
        int columnIndex = ReaderUtil.findColumnIndexByName(columnName, header);
        char type = header.getDBaseFields().get(columnIndex).getFieldtype();
        return type + "";
    }
}
