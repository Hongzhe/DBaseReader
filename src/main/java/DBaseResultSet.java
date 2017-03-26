import bean.DBaseField;
import bean.DBaseHeader;
import util.ReaderUtil;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;

/**
 * java.sql.ResultSet like
 * Created by Hongzhe on 3/25/2017.
 */
public class DBaseResultSet {

    private RandomAccessFile file;
    private DBaseHeader DBaseHeader;
    private long pointer;
    private int count;
    private long recordHead;
    private int[] fieldLengths;
    private int recordlength;

    public DBaseResultSet(DBaseHeader DBaseHeader, RandomAccessFile file) {
        this.DBaseHeader = DBaseHeader;
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
        if (count > DBaseHeader.getNumberOfRecords()) {
            return false;
        }
        pointer++; //skip the delete flag;
        recordHead = pointer;
        return true;
    }


    /**
     * Validate the column index
     * @param columnIndex
     * @throws DBaseReaderException
     */
    private void validateIndex(int columnIndex) throws DBaseReaderException{
        if (columnIndex < 0 || columnIndex >= DBaseHeader.getDBaseFields().size())
            throw new DBaseReaderException(columnIndex + " out of bound");
    }

    /**
     * Validate the column name
     * @param columnName
     * @throws DBaseReaderException
     */
    private void validateColumnName(String columnName) throws DBaseReaderException {
        if (columnName == null ) {
            throw new DBaseReaderException("null column name");
        }
        if (columnName.isEmpty()) {
            throw new DBaseReaderException("Empty column name");
        }
    }

    /**
     * 
     * @param columnindex
     * @return
     */
    private int computeFieldStart(int columnindex) {
        int pre = 0;
        for (int i = 0; i < columnindex; i++) {
            pre += fieldLengths[i];
        }
        return pre;
    }

    /**
     * find column index by the column name
     * @param columnName
     * @return
     * @throws DBaseReaderException
     */
    private int findColumnIndexByName(String columnName) throws DBaseReaderException {
        validateColumnName(columnName);
        int index = 0;
        boolean found = false;
        for (; index < DBaseHeader.getDBaseFields().size(); index++) {
            if (DBaseHeader.getDBaseFields().get(index).getName().equals(columnName)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new DBaseReaderException("unknown column " + columnName);
        }
        return index;
    }

    /**
     * Retrieve the designated column in the current row of this ResultSet object as String.
     * @param columnName
     * @return
     * @throws DBaseReaderException
     */
    public String getString (String columnName) throws DBaseReaderException {
        validateColumnName(columnName);
        int columnIndex = findColumnIndexByName(columnName);
        pointer = recordHead;
        pointer += computeFieldStart(columnIndex);
        String ret;
        try {
            byte[] bytes = ReaderUtil.readNBytes(file, pointer, DBaseHeader.getDBaseFields().get(columnIndex).getLength());
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
        validateIndex(columnIndex);
        pointer = recordHead;
        pointer += computeFieldStart(columnIndex);
        String ret = null;
        try {
            byte[] bytes = ReaderUtil.readNBytes(file, pointer, DBaseHeader.getDBaseFields().get(columnIndex).getLength());
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
        validateIndex(columnIndex);
        pointer = recordHead;
        pointer += computeFieldStart(columnIndex);
        BigDecimal decimal;
        try {
            byte[] bytes = ReaderUtil.readNBytes(file, pointer, DBaseHeader.getDBaseFields().get(columnIndex).getLength());
            String str = new String(bytes);
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
        validateColumnName(columnName);
        int columnIndex = findColumnIndexByName(columnName);
        pointer = recordHead;
        pointer += computeFieldStart(columnIndex);
        BigDecimal decimal;
        try {
            byte[] bytes = ReaderUtil.readNBytes(file, pointer, DBaseHeader.getDBaseFields().get(columnIndex).getLength());
            String str = new String(bytes);
            decimal = new BigDecimal(str);
        } catch (IOException e) {
            throw new DBaseReaderException(e);
        }
        return decimal;
    }
}
