package org.tea.plus.hdp.module.hottop;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Comparator;

/**
 * @author by mobingsen on 2021/6/2 22:44
 */
@Data
@Accessors(chain = true)
public class HotTopKey implements WritableComparable<HotTopKey> {

    private int year;
    private int month;
    private int day;
    private int wd;
    private String location;

    @Override
    public int compareTo(HotTopKey o) {
        return Comparator.comparingInt(HotTopKey::getYear)
                .thenComparingInt(HotTopKey::getMonth)
                .thenComparingInt(HotTopKey::getDay)
                .compare(this, o);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(year);
        out.writeInt(month);
        out.writeInt(day);
        out.writeInt(wd);
        out.writeUTF(location);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.year = in.readInt();
        this.month = in.readInt();
        this.day = in.readInt();
        this.wd = in.readInt();
        this.location = in.readUTF();
    }
}
