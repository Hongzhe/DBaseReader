package reader;

import bean.DBaseField;
import bean.DBaseHeader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;

/**
 * java.sql.ResultSet like
 * Created by Hongzhe on 3/25/2017.
 */
public class DBaseResultSet {

    private RandomAccessFile file;
    private DBaseHeader header;
    private long pointer;
    private int count;
    private long recordHead;
    private int[] fieldLengths;
    private int recordlength;

    public DBaseResultSet(DBaseHeader DBaseHeader, RandomAccessFile file) {
        this.header = DBaseHeader;
        this.file = file;
        pointer = DBaseHeader.getNumberOfBytes();
        fieldLengths = new int[DBaseHeader.getDBaseFields().size()];

        for (int i = 0; i < fieldLengths.length; i++) {
            DBaseField DBaseField = DBaseHeader.getDBaseFields().get(i);
            fieldLengths[i] = DBaseField.getLength();
            recordlength += fieldLengths[i];
        }
    }

    public boolean next() {
        if (count > header.getNumberOfRecords()) {
            return false;
        }
        if (count != 0) {
            pointer = recordHead;
            pointer += recordlength;
        }
        pointer ++; //skip delete flag
        recordHead = pointer;
        count++;
        return true;
    }

    /**
     * Retrieves the number, types and properties of this ResultSet object's columns.
     * @return
     */
    public DBaseResultSetMetaData getMetaData() {
        DBaseResultSetMetaData metaData = new DBaseResultSetMetaData(header);
        return metaData;
    }
    private int computeFieldStart(int columnindex) {
        int pre = 0;
        for (int i = 0; i < columnindex; i++) {
            pre += fieldLengths[i];
        }
        return pre;
    }

    /**
     * Retrieve the designated column in the current row of this ResultSet object as String.
     * @param columnName
     * @return
     * @throws DBaseReaderException
     */
    public String getString (String columnName) throws DBaseReaderException {
        ReaderUtil.validateColumnName(columnName, header);
        int columnIndex = ReaderUtil.findColumnIndexByName(columnName, header);
        pointer = recordHead;
        pointer += computeFieldStart(columnIndex);
        String ret;
        try {
            byte[] bytes = ReaderUtil.readNBytes(file, pointer, header.getDBaseFields().get(columnIndex).getLength());
            ret = new String(bytes);
        } catch (IOException e) {
            throw new DBaseReaderException(e);
        }
        return ret;
    }

    /**
     * Retrieve the designated column in the current row of this ResultSet object as String.
     * @param columnIndex
     * @return
     * @throws DBaseReaderException
     */
    public String getString(int columnIndex) throws DBaseReaderException {
        ReaderUtil.validateIndex(columnIndex, header);
        pointer = recordHead;
        pointer += computeFieldStart(columnIndex);
        String ret = null;
        try {
            byte[] bytes = ReaderUtil.readNBytes(file, pointer, header.getDBaseFields().get(columnIndex).getLength());
            ret = new String(bytes);
        } catch (IOException e) {
            throw new DBaseReaderException(e);
        }
        return ret;
    }


    /**
     * Retrieves the value of designated column in current row as BigDecimal
     * data with tyep number are stored as ascii chars in dBase
     * @param columnIndex
     * @return
     */
    public BigDecimal getNumber(int columnIndex) throws DBaseReaderException {
        ReaderUtil.validateIndex(columnIndex, header);
        pointer = recordHead;
        pointer += computeFieldStart(columnIndex);
        BigDecimal decimal;
        try {
            byte[] bytes = ReaderUtil.readNBytes(file, pointer, header.getDBaseFields().get(columnIndex).getLength());
            String str = new String(bytes);
            str = str.trim();
            decimal = new BigDecimal(str);
        } catch (IOException e) {
            throw new DBaseReaderException(e);
        }
        return decimal;
    }

    /**
     * Retrieves the value of designated column in current row as BigDecimal
     * data with tyep number are stored as ascii chars in dBase
     * @param columnName
     * @return
     */
    public BigDecimal getNumber(String columnName) throws DBaseReaderException {
        ReaderUtil.validateColumnName(columnName, header);
        int columnIndex = ReaderUtil.findColumnIndexByName(columnName, header);
        pointer = recordHead;
        pointer += computeFieldStart(columnIndex);
        BigDecimal decimal;
        try {
            byte[] bytes = ReaderUtil.readNBytes(file, pointer, header.getDBaseFields().get(columnIndex).getLength());
            String str = new String(bytes);
            decimal = new BigDecimal(str.trim());
        } catch (IOException e) {
            throw new DBaseReaderException(e);
        }
        return decimal;
    }
}
