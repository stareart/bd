package com.hym.hadoop.mr.topN;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class MyReducer extends Reducer<OrderBean, DoubleWritable, Text, DoubleWritable> {
    DateUtils dateUtils = new DateUtils();
    @Override
    protected void reduce(OrderBean key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
        //求每个用户、每个月、消费金额最多的两笔多少钱
        int num = 0;
        for(DoubleWritable value: values) {
            if(num < 2) {
                String keyOut = key.getUserid() + "  " + key.getDatetime();
                context.write(new Text(keyOut), value);
                num++;
            } else {
                break;
            }
        }

    }
}