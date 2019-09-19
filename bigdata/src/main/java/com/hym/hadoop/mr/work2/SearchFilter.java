package com.hym.hadoop.mr.work2;

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

/**
 * 2019-09-09:使用压缩将2019-9-6的作业的代码中设置压缩以减少磁盘io
 *
 * 结果：
 *          2.0 M    5.9 M   /data/20190910/mr2
 *          596.1 K  1.7 M   /data/20190910/mr3
 *
 */
public class SearchFilter {

    public static void main(String[] args) throws IOException,ClassNotFoundException,InterruptedException{
        if (args.length != 3 || args == null) {
            System.out.println("please input Path!");
            System.exit(0);
        }
        run(args);
    }

    public static boolean run(String[] args)throws IOException,ClassNotFoundException,InterruptedException{
        // 1: get configration
        Configuration conf = new Configuration();

        if("1".equals(args[2])){ // 是否压缩
            //开启map输出进行压缩的功能
            conf.set("mapreduce.map.output.compress", "true");
            //设置map输出的压缩算法是：BZip2Codec，它是hadoop默认支持的压缩算法，且支持切分
            conf.set("mapreduce.map.output.compress.codec", "org.apache.hadoop.io.compress.BZip2Codec");
            //开启job输出压缩功能
            conf.set("mapreduce.output.fileoutputformat.compress", "true");
            //指定job输出使用的压缩算法
            conf.set("mapreduce.output.fileoutputformat.compress.codec", "org.apache.hadoop.io.compress.BZip2Codec");
        }

        // 2: create Job
        Job job = Job.getInstance(conf, SearchFilter.class.getSimpleName());
        // run jar
        job.setJarByClass(SearchFilter.class);

        // 3: set job
        //input->mapper->output
        // 3.1 input
        FileInputFormat.addInputPath(job,new Path(args[0]));
        // 3.2 mapper
        job.setMapperClass(SearchFilter.CleanMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);
        // 3.3 output
        FileOutputFormat.setOutputPath(job,new Path(args[1]));

        // 设置不需要reducer
        job.setNumReduceTasks(0);

        return job.waitForCompletion(true);

    }

    public static class CleanMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

        NullWritable nullValue = NullWritable.get();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split("\t");
            if(fields.length!=6){
                return;
            }
            try{
                if(Integer.valueOf(fields[3])<2){
                    return ;
                }
                if(!fields[5].contains("zhidao.baidu.com")){
                    return ;
                }
            }catch (Exception e){
            }
            context.write(value,nullValue);

        }
    }
}
