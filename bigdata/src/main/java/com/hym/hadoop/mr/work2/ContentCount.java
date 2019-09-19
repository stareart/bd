package com.hym.hadoop.mr.work2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 2019-09-09：设定reducer分区数,然后按照设定的分区数进行分区
 *
 *      按照分钟统计搜索次数，分3个时间段（0-12、12-18、18-24）输出到不同的结果文件
 */
public class ContentCount {

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
        Job job = Job.getInstance(conf, ContentCount.class.getSimpleName());
        // run jar
        job.setJarByClass(ContentCount.class);

        // 3: set job
        //input->mapper->output
        // 3.1 input
        FileInputFormat.addInputPath(job,new Path(args[0]));
        // 3.2 mapper
        job.setMapperClass(ContentCount.MRPatitionerMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        // 3.3 reduce
        job.setReducerClass(ContentCount.MRReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        // 3.4 output
        FileOutputFormat.setOutputPath(job,new Path(args[1]));

        // 4.1 设置分区
        job.setNumReduceTasks(3);
        job.setPartitionerClass(ContentCountPartitioner.class);

        // 4.2 设置combiner
        job.setCombinerClass(ContentCount.MRReducer.class);

        return job.waitForCompletion(true);

    }

    public static class MRPatitionerMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

        IntWritable cnt = new IntWritable();
        Text content = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split("\t");
            if(fields.length!=6){
                return;
            }
            content.set(fields[0].substring(0,12));// 按分钟统计搜索次数
            cnt.set(1);
            context.write(content,cnt);
        }
    }


    public static class MRReducer extends Reducer<Text, IntWritable,Text, IntWritable>{
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0 ;
            for(IntWritable value:values){
                sum += value.get();
            }
            context.write(key,new IntWritable(sum));
        }
    }
}
