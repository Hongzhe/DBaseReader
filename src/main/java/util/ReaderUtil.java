package util;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Hongzhe on 3/25/2017.
 */
public class ReaderUtil {

    public static byte[] readNBytes(RandomAccessFile file, long pointer, int length) throws IOException {
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = file.readByte();
        }
        return bytes;
    }
    public static int readInt16(RandomAccessFile file, long pointer) throws IOException {
        file.seek(pointer);
        byte byte1 = file.readByte();
        byte byte2 = file.readByte();
        return byte1 & 0xff | (byte2 & 0xff) << 8;
    }
    public static int readInt32(RandomAccessFile file,long pointer) throws IOException {
        file.seek(pointer);
        return (file.readByte() & 0xff) | (file.readByte() & 0xff) << 8 | (file.readByte() & 0xff) << 16 | (file.readByte() & 0xff) << 24;
    }
}
