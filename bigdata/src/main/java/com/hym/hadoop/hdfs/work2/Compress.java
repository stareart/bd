package com.hym.hadoop.hdfs.work2;

import com.hym.hadoop.hdfs.HdfsFileSystem;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.util.ReflectionUtils;
import java.io.*;


public class Compress {

    public static void main(String[] args) throws ClassNotFoundException {

        String localPath ="D:\\hadoopSpace\\bigdata\\src\\main\\sources\\data\\sogou.50w.utf8";
        //压缩类
        String codecDefault = "org.apache.hadoop.io.compress.DefaultCodec";
        String codecGz = "org.apache.hadoop.io.compress.GzipCodec";
        String codecBZ = "org.apache.hadoop.io.compress.BZip2Codec";

        String targetPathDefault = "/data/hdfs/sogou.50w.utf8.default";
        String targetPathGz = "/data/hdfs/sogou.50w.utf8.Gz";
        String targetPathBZ = "/data/hdfs/sogou.50w.utf8.BZ";

        compress(localPath,targetPathDefault,codecDefault);
        compress(localPath,targetPathGz,codecGz);
        compress(localPath,targetPathBZ,codecBZ);


    }


    private static void compress(String localPath,String targetPath,String compressClass){
        System.out.println("开始压缩上传文件到HDFS，源文件文件："+localPath
                +"\t采用 ["+compressClass+"] 方法进行压缩，"
                +"上传到目标路径："+targetPath);
        InputStream inputStream = null;
        FileSystem fileSystem = null;
        FSDataOutputStream fsDataOutputStream =null;
        CompressionOutputStream outputStream=null;
        try{
            // 构造压缩类
            Class<?> cclass = Class.forName(compressClass);
            Configuration conf = new Configuration();
            CompressionCodec compressionCodec = (CompressionCodec) ReflectionUtils.newInstance(cclass, conf);

            inputStream = new BufferedInputStream(new FileInputStream(localPath));
            fileSystem = HdfsFileSystem.getFileSystem();

            fsDataOutputStream = fileSystem.create(new Path(targetPath));
            outputStream = compressionCodec.createOutputStream(fsDataOutputStream);

            IOUtils.copyBytes(inputStream,outputStream,4096,false);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                inputStream.close();
                fsDataOutputStream.close();
                outputStream.close();
            }catch (Exception e){
            }
        }
        System.out.println("完成压缩上传！");

    }
}