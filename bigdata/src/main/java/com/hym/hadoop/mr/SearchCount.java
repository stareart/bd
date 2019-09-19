package com.hym.hadoop.mr;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class SearchCount {


    public static class SearchCountMapper extends Mapper<LongWritable,Text , Text,IntWritable>{

        Text userID = new Text();
        IntWritable cnt = new IntWritable();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] fields = value.toString().split("\t");
            userID.set(fields[1]);
            context.write(userID,cnt);
        }
    }

    public static class SearchCountReducer extends Reducer<Text,IntWritable,Text,IntWritable> {

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            super.reduce(key, values, context);
        }
    }

}
