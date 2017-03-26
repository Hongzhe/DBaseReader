import bean.Field;
import bean.Header;
import org.apache.log4j.Logger;
import util.ReaderUtil;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

import static util.ReaderUtil.*;


/**
 * Created by Hongzhe on 3/24/2017.
 */
public class DBaseReader {

    private int recordcount;
    private Header header;
    private RandomAccessFile file;
    public DBaseReader() {
        recordcount = 0;
    }

    private static Logger log = Logger.getLogger(DBaseReader.class);

    public void read(String path) throws IOException {
        file = null;
        try {
            file = new RandomAccessFile(path, "r");
            int flag = file.readByte();
            int pointer = 1;
            byte[] datebyte = ReaderUtil.readNBytes(file, pointer, 3);
            String date = new String(datebyte);
            header = new Header();
            header.setLastupdate(new Date(date));
            pointer += 3;
            header.setNumberOfRecords(readInt32(file, pointer));
            pointer += 4;
            int headerbytes = readInt16(file,pointer);
            header.setNumberOfBytes(headerbytes);
            pointer += 2;
            header.setGetNumberOfBytesInRecords(readInt16(file, pointer));
            int numberOfFields = (headerbytes - 33) / 32;
            for (int i = 0; i < numberOfFields; i++) {
               Field field =  readField(file, pointer);
               header.addFields(field);
               pointer += 32;
            }
        } finally {
            if (file != null) file.close();
        }
    }


    private Field readField(RandomAccessFile file, long pointer) throws IOException {
        Field field = new Field();
        file.seek(pointer);
        byte[] namebytes = ReaderUtil.readNBytes(file, pointer, 11);
        String name = new String(namebytes);
        field.setName(name);
        char datatype = (char)file.readByte();
        field.setFieldtype(datatype);
        file.skipBytes(4);
        field.setLength(file.readByte());
        field.setDecimalcount(file.readByte());
        return field;
    }

    public static void main(String[] args) {
        log.info("test properties");
    }
}
