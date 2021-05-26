package com.drop.leaves.hdp.module.mr.wc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @author by mobingsen on 2021/5/25 20:31
 */
public class MyJob {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = new Configuration(true);

        // 让框架知道是windows异构平台运行
        conf.set("mapreduce.app-submission.cross-platform", "true");
        // 加上这句就是本地单机运行，job.setJar("C:\\dev\\IdeaCodes\\hadoop-demo\\target\\hadoop-demo-0.0.1-SNAPSHOT.jar");可以注释掉了
        conf.set("mapreduce.framework.name", "local");

        Job job = Job.getInstance(conf);

        // 分布式运行需要知道jar包，方便拉取jar包
        //job.setJar("C:\\dev\\IdeaCodes\\hadoop-demo\\target\\hadoop-demo-0.0.1-SNAPSHOT.jar");

        job.setJarByClass(MyJob.class);
        job.setJobName(MyJob.class.getName());

        Path in = new Path("/data/wc/in");
        FileInputFormat.addInputPath(job, in);

        Path out = new Path("/data/wc/out");
        FileSystem fileSystem = out.getFileSystem(conf);
        if (fileSystem.exists(out)) {
            fileSystem.delete(out, true);
        }
        FileOutputFormat.setOutputPath(job, out);

        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setReducerClass(MyReducer.class);

        job.waitForCompletion(true);
    }
}
