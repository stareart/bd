package com.hym.hadoop.mr.inputformat;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

/**
 * 自定义InputFormat类；
 * 泛型：
 *  键：因为不需要使用键，所以设置为NullWritable
 *  值：值用于保存小文件的内容，此处使用BytesWritable
 */
public class WholeFileInputFormat extends FileInputFormat<NullWritable, BytesWritable> {

    /**
     *
     * 返回false，表示输入文件不可切割
     * @param context
     * @param file
     * @return
     */
    @Override
    protected boolean isSplitable(JobContext context, Path file) {
        return false;
    }

    /**
     * 生成读取分片split的RecordReader
     * @param split
     * @param context
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public RecordReader<NullWritable, BytesWritable> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException,InterruptedException {
        //使用自定义的RecordReader类
        WholeFileRecordReader reader = new WholeFileRecordReader();
        //初始化RecordReader
        reader.initialize(split, context);
        return reader;
    }
}