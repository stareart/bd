package com.hym.hadoop.hdfs.work1;


import com.hym.hadoop.hdfs.HdfsFileSystem;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HdfsDirectory {

    private static SimpleDateFormat  sdf = new SimpleDateFormat("yyyyMMdd");

    public static void main(String[] args) {
        String dir1 = "/data";
        String dir2 = "/data/"+ sdf.format(new Date());
        // 创建目录
//        createDirectory(dir1);
        createDirectory(dir2);

//        //3.列出某一个文件夹下的所有文件
//        System.out.println("===========列出某一个文件夹下的所有文件");
//        scanFileList(dir1,false);
//        //4.列出多级目录名称和目录下的文件名称
//        System.out.println("===========列出多级目录名称和目录下的文件名称");
//        scanFileList(dir1,true);
    }


    /**
     * 扫描目录
     * @param dirPath
     * @param recursion 是否递归且列出目录
     */
    public static void scanFileList(String dirPath,boolean recursion){
        Path path = null;
        FileSystem fileSystem = null;
        try{
            path = new Path(dirPath);
            fileSystem = HdfsFileSystem.getFileSystem();
            if(!fileSystem.exists(path)){
                System.out.println("目录不存在");
                return;
            }
            FileStatus[] fileStatuses = fileSystem.listStatus(path);
            for(FileStatus fileStatus:fileStatuses){
                if(fileStatus.isDirectory()){
                    if(recursion){
                        System.out.println("扫描到目录："+fileStatus.getPath().toString());
                        scanFileList(fileStatus.getPath().toString(),recursion);
                    }
                }else{
                    System.out.println("扫描到文件："+fileStatus.getPath());
                }
            }
        }catch (Exception e){

        }
    }

    /**
     * 创建目录
     * @param dirPath
     * @return
     */
    public static boolean createDirectory(String dirPath){
        Path path = null;
        FileSystem fileSystem = null;
        boolean mkdirs =false;
        try{
            path = new Path(dirPath);
            fileSystem = HdfsFileSystem.getFileSystem();
            if(fileSystem.exists(path)){
                System.out.println("目录已存在："+dirPath);
                return false;
            }
            mkdirs = fileSystem.mkdirs(path);

        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        if(mkdirs){
            System.out.println("成功创建目录："+dirPath);
            return true;
        }
        System.out.println("创建目录失败：{}"+dirPath);
        return false;
    }

}
