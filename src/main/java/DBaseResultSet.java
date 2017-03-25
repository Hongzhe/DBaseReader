import bean.Field;
import bean.Header;
import util.ReaderUtil;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.List;

/**
 * java.sql.ResultSet like
 * Created by Hongzhe on 3/25/2017.
 */
public class DBaseResultSet {

    private RandomAccessFile file;
    private Header header;
    private long pointer;
    private int count;
    private long recordHead;
    private int[] fieldLengths;
    private int recordlength;
    public DBaseResultSet(Header header, RandomAccessFile file) {
        this.header = header;
        this.file = file;
        pointer = header.getNumberOfBytes();
        fieldLengths = new int[header.getFields().size()];

        for (int i = 0; i < fieldLengths.length; i++) {
            Field field = header.getFields().get(i);
            fieldLengths[i] = field.getLength();
            recordlength += fieldLengths[i];
        }
    }

    public boolean next() {
        if (count > header.getNumberOfRecords()) {
            return false;
        }
        pointer++; //skip the delete flag;
        recordHead = pointer;
        return true;
    }

    private void validateIndex(int columnIndex) throws DBaseReaderException{
        if (columnIndex < 0 || columnIndex >= header.getFields().size())
            throw new DBaseReaderException(columnIndex + " out of bound");
    }

    private int computeFieldStart(int columnindex) {
        int pre = 0;
        for (int i = 0; i < columnindex; i++) {
            pre += fieldLengths[i];
        }
        return pre;
    }

    public int getInt(int columnIndex) throws DBaseReaderException {
        validateIndex(columnIndex);
        Field field = header.getFields().get(columnIndex);
        char type = field.getFieldtype();
        int length = field.getLength();
        pointer = recordHead;
        pointer += computeFieldStart(columnIndex);
        try {
            return ReaderUtil.readInt32(file, pointer);
        } catch(IOException e) {
            throw new DBaseReaderException(e);
        }
    }

    public int getInt(String columnName) throws DBaseReaderException {
        int columnIndex = -1;
        boolean found = false;
        Iterator<Field> fields = header.getFields().iterator();
        while(fields.hasNext()) {
            columnIndex++;
            if(fields.next().getName().equals(columnName)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new DBaseReaderException("unknown column name " + columnName);
        }
        pointer = recordHead;
        pointer += computeFieldStart(columnIndex);
        try {
            return ReaderUtil.readInt32(file, pointer);
        } catch(IOException e) {
            throw new DBaseReaderException(e);
        }
    }

    public String getString(int columnIndex) {
        return "";
    }

    public String getString (String columnName) {
        return "";
    }

    public Object getNumberic(int columnIndex) {
        return null;
    }

    public Object getNumberic(String columnName) {
        return null;
    }
}
