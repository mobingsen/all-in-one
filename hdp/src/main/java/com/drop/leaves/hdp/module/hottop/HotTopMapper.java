package com.drop.leaves.hdp.module.hottop;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Created by mobingsen on 2021/6/2 22:57
 */
public class HotTopMapper extends Mapper<LongWritable, Text, HotTopKey, IntWritable> {

    HotTopKey k = new HotTopKey();
    IntWritable v = new IntWritable();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        List<String> strings = Arrays.stream(value.toString().split("\t"))
                .distinct()
                .collect(Collectors.toList());
        LocalDateTime time = LocalDateTime.parse(strings.get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int wd = Integer.parseInt(strings.get(2));
        k.setYear(time.getYear())
                .setMonth(time.getMonthValue())
                .setDay(time.getDayOfMonth())
                .setWd(wd);
        v.set(wd);
        context.write(k, v);
    }
}
