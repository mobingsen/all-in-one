package com.drop.leaves.hdp.module.hottop;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Created by mobingsen on 2021/6/2 22:57
 */
public class HotTopMapper extends Mapper<LongWritable, Text, HotTopKey, IntWritable> {

    HotTopKey k = new HotTopKey();
    IntWritable v = new IntWritable();

    private Map<String, String> dict;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        URI[] files = context.getCacheFiles();
        for (URI uri : files) {
            Path path = new Path(uri.getPath());
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path.getName()));
            dict = bufferedReader.lines()
                    .map(line -> line.split("\t"))
                    .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));
            bufferedReader.close();
        }
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 2021-06-03 23:18:05  1   27
        List<String> strings = Arrays.stream(value.toString().split("\t"))
                .distinct()
                .collect(Collectors.toList());
        LocalDateTime time = LocalDateTime.parse(strings.get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int wd = Integer.parseInt(strings.get(2));
        k.setYear(time.getYear())
                .setMonth(time.getMonthValue())
                .setDay(time.getDayOfMonth())
                .setWd(wd)
                .setLocation(dict.get(strings.get(1)));
        v.set(wd);
        context.write(k, v);
    }
}
