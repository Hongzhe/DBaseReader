package bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Hongzhe on 3/24/2017.
 */
public class DBaseHeader {

    //date is formatted as YYMMDD;
    private String lastupdate;

    private int numberOfRecords;
    private int getNumberOfBytesInRecords;
    private int numberOfBytes;
    private List<DBaseField> DBaseFields;

    public DBaseHeader() {
        DBaseFields = new ArrayList<>();
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
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

    public List<DBaseField> getDBaseFields() {
        return DBaseFields;
    }

    public void addFields(DBaseField DBaseField) {
        DBaseFields.add(DBaseField);
    }
}
