package org.tea.plus.hdp.module.hottop;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * @author by mobingsen on 2021/6/2 23:11
 */
public class HotTopPartitioner extends Partitioner<HotTopKey, IntWritable> {
    @Override
    public int getPartition(HotTopKey k, IntWritable v, int numPartitions) {
        // 数据倾斜
        return k.getYear() % numPartitions;
    }
}
