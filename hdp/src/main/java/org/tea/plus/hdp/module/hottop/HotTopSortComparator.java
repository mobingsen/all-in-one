package org.tea.plus.hdp.module.hottop;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import java.util.Comparator;

/**
 * @author by mobingsen on 2021/6/2 23:15
 */
public class HotTopSortComparator extends WritableComparator {

    public HotTopSortComparator() {
        super(HotTopKey.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        return Comparator.comparingInt(HotTopKey::getYear)
                .thenComparingInt(HotTopKey::getMonth)
                .thenComparing(Comparator.comparing(HotTopKey::getWd).reversed())
                .compare((HotTopKey) a, (HotTopKey) b);
    }
}
