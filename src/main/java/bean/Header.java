package bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Hongzhe on 3/24/2017.
 */
public class Header {

    private Date lastupdate;
    private int numberOfRecords;
    private int getNumberOfBytesInRecords;
    private int numberOfBytes;
    private List<Field> fields;

    public Header() {
        fields = new ArrayList<>();
    }

    public Date getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(Date lastupdate) {
        this.lastupdate = lastupdate;
    }

    public int getNumberOfRecords() {
        return numberOfRecords;
    }

    public void setNumberOfRecords(int numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }

    public int getGetNumberOfBytesInRecords() {
        return getNumberOfBytesInRecords;
    }

    public void setGetNumberOfBytesInRecords(int getNumberOfBytesInRecords) {
        this.getNumberOfBytesInRecords = getNumberOfBytesInRecords;
    }

    public int getNumberOfBytes() {
        return numberOfBytes;
    }

    public void setNumberOfBytes(int numberOfBytes) {
        this.numberOfBytes = numberOfBytes;
    }

    public List<Field> getFields() {
        return fields;
    }

    private void addFields(Field field) {
        fields.add(field);
    }
}
