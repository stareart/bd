package com.hym.hadoop.mr.work2;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class ContentCountPartitioner extends Partitioner<Text, IntWritable> {


    /**
     * 分时间段输出搜索统计结果 0-12、12-18、18-24
     * @param text
     * @param intWritable
     * @param i
     * @return
     */
    @Override
    public int getPartition(Text text, IntWritable intWritable, int i) {

        Integer integer =0;
        try{
            String substring = text.toString().substring(8, 10);
            integer = Integer.valueOf(substring);
        }catch (Exception e){
            return 0;
        }
        if(integer<12){
            return 0;
        }else if(integer<18){
            return 1;
        }
        return 2;

    }
}
