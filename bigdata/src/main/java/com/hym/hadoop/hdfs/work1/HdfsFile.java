package com.hym.hadoop.hdfs.work1;

import com.hym.hadoop.hdfs.HdfsFileSystem;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.*;

public class HdfsFile {

    public static void main(String[] args) {
        String fileName = "PutData.txt";
        String localPath = "D:\\hadoopWorkspace\\hdfs\\src\\main\\resources\\";
        String targetPath = "/data/";
        // 1.向hdfs中,上传一个文本文件
        System.out.println("1.向hdfs中,上传一个文本文件");
        putFile(localPath+fileName,targetPath);

        // 2.读取hdfs上的文件
        System.out.println("2.读取hdfs上的文件");
        readFile(targetPath+fileName);
    }


    /**
     * 文件上传
     * @param localSrc
     * @param hdfsPath
     * @return boolean
     */
    public static boolean putFile(String localSrc,String hdfsPath) {
        File file = null;
        InputStream inputStream = null;
        FSDataOutputStream fsDataOutputStream = null;

        try {
            file = new File(localSrc);
            inputStream = new BufferedInputStream(new FileInputStream(file));
            fsDataOutputStream = HdfsFileSystem.getFileSystem().create(new Path(hdfsPath + file.getName()));
            IOUtils.copyBytes(inputStream,fsDataOutputStream, 4096,false);

        }catch (FileNotFoundException e){
            e.printStackTrace();
            return false;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }finally {
            if(inputStream != null){
                IOUtils.closeStream(inputStream);
            }
            if(fsDataOutputStream != null){
                IOUtils.closeStream(fsDataOutputStream);
            }
        }
        System.out.println("完成文件上传：\n\t"+localSrc+"\t=====>\t"+hdfsPath+file.getName());
        return true;

    }


    /**
     * 读取文件
     * @param filePath
     */
    public static void readFile(String filePath){
        FSDataInputStream open = null ;
        BufferedReader bufferedReader = null;
        try {
            open = HdfsFileSystem.getFileSystem().open(new Path(filePath));
            bufferedReader = new BufferedReader(new InputStreamReader(open, "UTF-8"));
            String line;
            while((line=bufferedReader.readLine())!=null){
                System.out.println(line);
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(bufferedReader != null){
                IOUtils.closeStream(bufferedReader);
            }
        }

    }


}
