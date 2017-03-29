import org.junit.Test;
import reader.DBaseReader;

import java.io.IOException;

/**
 * Created by Hongzhe on 3/26/2017.
 */
public class DBaseReaderTest {

    @Test
    public void testConnect() throws IOException {
        DBaseReader reader = new DBaseReader();
        reader.read("ROAD_LINK.DAT");
    }
}
