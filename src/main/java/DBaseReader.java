import java.io.IOException;
import java.io.RandomAccessFile;


/**
 * Created by Hongzhe on 3/24/2017.
 */
public class DBaseReader {

    public void read(String path) throws IOException {
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(path, "r");
            int flag = file.readByte();

        } finally {
            if (file != null) file.close();
        }
    }
}
