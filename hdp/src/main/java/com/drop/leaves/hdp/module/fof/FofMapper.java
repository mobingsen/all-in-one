package com.drop.leaves.hdp.module.fof;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author by mobingsen on 2021/6/5 13:28
 */
public class FofMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    Text k = new Text();
    IntWritable v = new IntWritable();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] strings = value.toString().split(" ");
        if (strings.length > 1) {
            String one = strings[0];
            for (int i = 1; i < strings.length; i++) {
                k.set(one.compareTo(strings[i]) > 0 ? one + "-" + strings[i] : strings[i] + "-" + one);

            }
        }
    }
}
