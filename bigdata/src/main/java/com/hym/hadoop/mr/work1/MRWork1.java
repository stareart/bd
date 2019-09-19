package com.hym.hadoop.mr.work1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class MRWork1 {

    public static void main(String[] args) throws IOException,ClassNotFoundException,InterruptedException{
        if (args.length != 2 || args == null) {
            System.out.println("please input Path!");
            System.exit(0);
        }
        run(args);
    }

    public static boolean run(String[] args)throws IOException,ClassNotFoundException,InterruptedException{
        // 1: get configration
        Configuration conf = new Configuration();

        // 2: create Job
        Job job = Job.getInstance(conf, MRWork1.class.getSimpleName());
        // run jar
        job.setJarByClass(MRWork1.class);

        // 3: set job
        //input->mapper->output
        // 3.1 input
        FileInputFormat.addInputPath(job,new Path(args[0]));
        // 3.2 mapper
        job.setMapperClass(UrlAdjustMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);
        // 3.3 output
        FileOutputFormat.setOutputPath(job,new Path(args[1]));

        // 设置不需要reducer
        job.setNumReduceTasks(0);

        return job.waitForCompletion(true);

    }



    public static class UrlAdjustMapper extends Mapper<LongWritable, Text, Text, NullWritable>{

        NullWritable nullValue = NullWritable.get();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split("\t");
            if(fields.length!=6){
                return;
            }
            context.write(new Text(fields[2]+"\t"+fields[0]+"\t"+fields[1]+"\t"+fields[3]+"\t"+fields[4]+"\t"+fields[5]),nullValue);

        }
    }



}
