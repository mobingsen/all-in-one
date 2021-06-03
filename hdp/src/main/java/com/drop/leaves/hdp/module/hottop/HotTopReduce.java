package com.drop.leaves.hdp.module.hottop;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * @author by mobingsen on 2021/6/2 23:30
 */
public class HotTopReduce extends Reducer<HotTopKey, IntWritable, Text, IntWritable> {

    Text k = new Text();
    IntWritable v = new IntWritable();

    @Override
    protected void reduce(HotTopKey key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int flag = 0;
        int day = 0;
        Iterator<IntWritable> iterator = values.iterator();
        while (iterator.hasNext()) {
            if (flag == 0) {
                k.set(key.getYear() + "-" + key.getMonth() + "-" + key.getDay() + "@" + key.getLocation());
                v.set(key.getWd());
                context.write(k, v);
                flag++;
                day = key.getDay();
            }
            if (day != key.getDay()) {
                k.set(key.getYear() + "-" + key.getMonth() + "-" + key.getDay() + "@" + key.getLocation());
                v.set(key.getWd());
                context.write(k, v);
                break;
            }
        }

    }
}
