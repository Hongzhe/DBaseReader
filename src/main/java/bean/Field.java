package bean;

/**
 * Table field
 * Created by Hongzhe on 3/24/2017.
 */
public class Field {

    private String name;
    private char fieldtype;
    private int length;
    private int decimalcount;

    public Field() {}

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {return name;}

    public char getFieldtype() {
        return fieldtype;
    }

    public void setFieldtype(char fieldtype) {
        this.fieldtype = fieldtype;
    }

    public void setLength(int length) {
        this.length = length;
    }
    public int getLength() { return this.length; }

    public int getDecimalcount() {
        return decimalcount;
    }

    public void setDecimalcount(int decimalcount) {
        this.decimalcount = decimalcount;
    }

}
