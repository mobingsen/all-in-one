package com.drop.leaves.hdp.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

/**
 * @author by mobingsen on 2021/5/23 10:18
 */
class TestHdfs {

    private static Configuration conf = null;
    private static FileSystem fs = null;

    @BeforeEach
    void before() throws IOException, InterruptedException {
        System.setProperty("hadoop.home.dir", "C:\\dev\\hadoop-2.7.2");
        conf = new Configuration(true);
//        fs = FileSystem.get(conf);
        fs = FileSystem.get(URI.create("hdfs://mbs01:9000"), conf, "mbs");
    }

    @Test
    void mkdir() throws Exception {
        Path dir = new Path("/dropleaves");
        if (fs.exists(dir)) {
            fs.delete(dir, true);
        }
        fs.mkdirs(dir);
    }

    @Test
    void upload() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("data/test.txt");
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        Path outPath = new Path("/dropleaves/test/test.txt");
        FSDataOutputStream fsDataOutputStream = fs.create(outPath);
        IOUtils.copyBytes(bufferedInputStream, fsDataOutputStream, conf, true);
    }

    @Test
    void blocks() throws IOException {
        Path file = new Path("/dropleaves/test/test.txt");
        FileStatus fileStatus = fs.getFileStatus(file);
        BlockLocation[] fileBlockLocations = fs.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
        for (BlockLocation fileBlockLocation : fileBlockLocations) {
            System.out.println(fileBlockLocation);
        }
    }

    @AfterEach
    void after() throws IOException {
        fs.close();
    }
}
