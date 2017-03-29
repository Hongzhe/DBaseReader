package reader;

import bean.DBaseField;
import bean.DBaseHeader;
import reader.DBaseReaderException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * Created by Hongzhe on 3/25/2017.
 */
public class ReaderUtil {

    protected static byte[] readNBytes(RandomAccessFile file, long pointer, int length) throws IOException {
        file.seek(pointer);
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = file.readByte();
        }
        return bytes;
    }

    protected static int readInt16(RandomAccessFile file, long pointer) throws IOException {
        file.seek(pointer);
        byte byte1 = file.readByte();
        byte byte2 = file.readByte();
        return byte1 & 0xff | (byte2 & 0xff) << 8;
    }

    protected static int readInt32(RandomAccessFile file,long pointer) throws IOException {
        file.seek(pointer);
        return (file.readByte() & 0xff) | (file.readByte() & 0xff) << 8 | (file.readByte() & 0xff) << 16 | (file.readByte() & 0xff) << 24;
    }

    protected static void validateIndex(int columnIndex, DBaseHeader header) throws DBaseReaderException {
        if (columnIndex < 0 || columnIndex > header.getDBaseFields().size())
            throw new DBaseReaderException();
    }

    protected static void validateColumnName(String columnName, DBaseHeader header) throws DBaseReaderException {
        if (columnName == null || columnName.isEmpty()) {
            throw new DBaseReaderException("Null or empty column name");
        }
    }

    protected static int findColumnIndexByName(String columnName, DBaseHeader header) throws DBaseReaderException {
        List<DBaseField> fields = header.getDBaseFields();
        for(int i = 0; i < fields.size(); i++) {
            if (fields.get(i).getName().equals(columnName)) {
                return i;
            }
        }
        throw new DBaseReaderException("Unknown column " + columnName);
    }
}
