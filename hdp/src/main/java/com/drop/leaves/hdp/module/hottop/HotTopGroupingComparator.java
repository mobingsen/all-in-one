package com.drop.leaves.hdp.module.hottop;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import java.util.Comparator;

/**
 * @author by mobingsen on 2021/6/2 23:27
 */
public class HotTopGroupingComparator extends WritableComparator {

    public HotTopGroupingComparator() {
        super(HotTopKey.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        return Comparator.comparingInt(HotTopKey::getYear)
                .thenComparingInt(HotTopKey::getMonth)
                .compare((HotTopKey) a, (HotTopKey) b);
    }
}
