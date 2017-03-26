import bean.DBaseField;
import bean.DBaseHeader;
import org.apache.log4j.Logger;
import util.ReaderUtil;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static util.ReaderUtil.*;


/**
 * Created by Hongzhe on 3/24/2017.
 */
public class DBaseReader {

    private DBaseHeader dBaseHeader;

    private RandomAccessFile file;

    public DBaseReader() { }

    private static Logger log = Logger.getLogger(DBaseReader.class);

    public void read(String path) throws IOException {
        file = null;
        try {
            file = new RandomAccessFile(path, "r");
            int flag = file.readByte();
            int pointer = 1;
            byte[] datebyte = ReaderUtil.readNBytes(file, pointer, 3);
            String datestr = parseDate(datebyte);
            dBaseHeader = new DBaseHeader();
            dBaseHeader.setLastupdate(datestr);
            pointer += 3;
            dBaseHeader.setNumberOfRecords(readInt32(file, pointer));
            pointer += 4;
            int headerbytes = readInt16(file,pointer);
            dBaseHeader.setNumberOfBytes(headerbytes);
            pointer += 2;
            dBaseHeader.setGetNumberOfBytesInRecords(readInt16(file, pointer));
            int numberOfFields = (headerbytes - 33) / 32;
            for (int i = 0; i < numberOfFields; i++) {
               DBaseField dBaseField =  readField(file, pointer);
               dBaseHeader.addFields(dBaseField);
               pointer += 32;
            }
        } finally {
            if (file != null) file.close();
        }
    }

    public DBaseHeader getHeader() { return dBaseHeader; }

    private String parseDate(byte[] dateBytes) {
        int yy = (int) dateBytes[0];
        int mm = (int) dateBytes[1];
        int dd = (int) dateBytes[2];
        return yy+""+mm+""+dd;
    }
    /**
     * Read dBase field
     * @param file
     * @param pointer
     * @return
     * @throws IOException
     */
    private DBaseField readField(RandomAccessFile file, long pointer) throws IOException {
        DBaseField DBaseField = new DBaseField();
        file.seek(pointer);
        byte[] namebytes = ReaderUtil.readNBytes(file, pointer, 11);
        String name = new String(namebytes);
        DBaseField.setName(name);
        char datatype = (char)file.readByte();
        DBaseField.setFieldtype(datatype);
        file.skipBytes(4);
        DBaseField.setLength(file.readByte());
        DBaseField.setDecimalcount(file.readByte());
        return DBaseField;
    }

    public static void main(String[] args) throws IOException {
        DBaseReader reader = new DBaseReader();
        reader.read("./target/classes/京承高速轨迹点.DAT");
        DBaseHeader header = reader.getHeader();
        log.info(header.getLastupdate());
        log.info(header.getDBaseFields().size());
        log.info(header.getNumberOfRecords());

    }
}
