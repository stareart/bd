package com.hym.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;

public class HdfsFileSystem {

    private static final String URL = "hdfs://hadoop101:8020";

    private static FileSystem fileSystem;

    public static FileSystem getFileSystem() throws IOException {
        if(fileSystem==null){
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", URL);
            fileSystem= FileSystem.get(conf);
        }
        return fileSystem;
    }
}
