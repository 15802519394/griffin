package org.apache.griffin.core.util;

import com.google.common.io.FileBackedOutputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.protocol.HdfsFileStatus;
import org.apache.hadoop.io.IOUtils;
import org.mortbay.util.IO;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:linshuo(170285)
 * @Description
 * @Date：14:40 2019/1/31
 * @Modified By :
 * @Company : ZLST
 */
public class HdfsOperateUtil {

    public static void main(String[] args){
        // 获取目录下级目录列表
//        String srcPath = "hdfs://bigdata121:9000/griffin";
//        List<String> fileList = getFileList(srcPath);
//        if(!fileList.isEmpty() && 0 != fileList.size() ){
//            for(String s : fileList){
//                System.out.println("获取hdfs路径下的文件列表 ===  "+s);
//            }
//        }

        // 创建文件并导入内容
//        String dst = "abc";
//        String str = "获取hdfs路径下的文件列表";
//        byte[] content = str.getBytes();
//        try {
//            createFile(dst,content);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // 创建目录
//        String path = "hdfs://bigdata121:9000/test001";
//        try {
//            mkdir(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // 删除目录或文件
//        String path = "hdfs://bigdata121:9000/user/170285";
//        try {
//            deleteFile(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // 读取文件数据，并在控制台打印
//        String path = "hdfs://bigdata121:9000/griffin/persist/accu_batch/1548300706563/myRecords";
//        try {
//            readFile(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // 下载hdfs文件到本地
//        String srcPath = "hdfs://bigdata121:9000/user/root/3.txt";
//        String dstPath = "E:\\logs\\test";
//        try {
//            downloadFile(srcPath,dstPath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // 上传
//        String hdfsPath = "hdfs://bigdata121:9000/test1";
//        String localPath = "E:\\logs\\data";
//        try {
//            uploadFileToHdfs(hdfsPath,localPath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // 重命名
        String oldName = "hdfs://bigdata121:9000/test1blsmy.txt";
        String newName = "hdfs://bigdata121:9000/test0012";
        try {
            renameFile(oldName,newName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取hdfs路径下的文件列表
     * @return
     */
    public static List<String> getFileList(String srcPath){
        List<String> fileList = new ArrayList<>();
        Configuration conf = new Configuration();
        Path path = new Path(srcPath);
        try {
            FileSystem fs = path.getFileSystem(conf);
            if(fs.exists(path) && fs.isDirectory(path)){
                for(FileStatus sa : fs.listStatus(path)){
                    fileList.add(sa.getPath().toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileList;
    }

    /**
     *  创建文件和内容
     *  （默认创建在当前用户目录下）
     * @param dst ：文件名
     * @param contents ：文件内容
     */
    public static void createFile(String dst,byte[] contents) throws IOException {
        // 设置登陆hdfs的用户（如果不设置，默认为操作系统当前用户）
        //System.setProperty("HADOOP_USER_NAME","root");

        Configuration conf = new Configuration();
        // 设置hdfs的NameNode地址；也可以通过把hadoop的 core-site.xml 和 hdfs-site.xml 放入工程
        conf.set("fs.defaultFS","hdfs://bigdata121:9000");

        Path dstPath = new Path(dst);
        FileSystem fs = dstPath.getFileSystem(conf);
        FSDataOutputStream out = fs.create(dstPath);
        out.write(contents);
        out.close();
    }

    /**
     * 创建hdfs目录
     * @param path （可以只写目录名称，也可以写全局路径）
     *  ( 只有目录名称时，是放在user/170285目录下
     *    全局路径：hdfs://bigdata121:9000/test002
     */
    public static void mkdir(String path) throws IOException {
        // 设置登陆hdfs的用户（如果不设置，默认为操作系统当前用户）
        System.setProperty("HADOOP_USER_NAME","root");

        Configuration conf = new Configuration();
        // 设置hdfs的NameNode地址；也可以通过把hadoop的 core-site.xml 和 hdfs-site.xml 放入工程
        conf.set("fs.defaultFS","hdfs://bigdata121:9000");

        Path dpath = new Path(path);
        FileSystem fs = dpath.getFileSystem(conf);
        boolean bool = fs.mkdirs(dpath);
        if(bool){
            System.out.println("create dir "+path+" success!");
        }else{
            System.out.println("create dir "+path+" failed!");
        }
    }

    /**
     * 文件目录的删除 (目录删除时包含子目录及其文件全部删除)
     * @param filePath
     * @throws IOException
     */
    public static void deleteFile(String filePath) throws IOException {
        Configuration conf = new Configuration();
        Path path = new Path(filePath);
        FileSystem fs = path.getFileSystem(conf);
        boolean bool = fs.deleteOnExit(path);
        if(bool){
            System.out.println("delete file " + filePath + " success !");
        }else{
            System.out.println("delete file " + filePath + " failed !");
        }
    }

    /**
     * 读取hdfs文件内容,并在控制台打印
     * @param srcPath
     */
    public static void readFile(String srcPath) throws IOException {
        Configuration conf = new Configuration();
        Path path = new Path(srcPath);
        FileSystem fs = null;
        try {
            URI uri = new URI(srcPath);
            fs = FileSystem.get(uri,conf);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        InputStream in = fs.open(path);
        IOUtils.copyBytes(in,System.out,4096,false);

        IOUtils.closeStream(in);
    }

    /**
     * 下载hdfs文件到本地目录
     * @param srcPath ： hdfs文件目录
     * @param dstPath ： 本地文件目录
     */
    public static void downloadFile(String srcPath,String dstPath) throws IOException {
        Configuration conf = new Configuration();
        Path path = new Path(srcPath);
        FileSystem fs = path.getFileSystem(conf);

        File rootFile = new File(dstPath);
        if(!rootFile.exists()){
            rootFile.mkdirs();
        }

        // 如果是一个文件
        if(fs.isFile(path)){
            String fileName = path.getName();
            // 非txt文件
            //if(!fileName.toLowerCase().endsWith("txt")){
                FSDataInputStream in = null;
                FileOutputStream out = null;
                try{
                    in = fs.open(path);
                    File srcFile =  new File(rootFile,path.getName());
                    if(!srcFile.exists()){
                        srcFile.createNewFile();
                    }
                    out = new FileOutputStream(srcFile);
                    IOUtils.copyBytes(in,out,4096,false);
                } finally {
                    IOUtils.closeStream(in);
                    IOUtils.closeStream(out);
                }
            //}
        } // 如果是一层目录
        else if(fs.isDirectory(path)){
            File dstDir = new File(dstPath);
            if(!dstDir.exists()){
                dstDir.mkdirs();
            }
            // 在本地目录创建一层子目录，方便下载的文件查看
            String filePath = path.toString();
            String subPath[] = filePath.split("/");
            // 获取最底层目录 (放在windows系统的目录)
            String newdstPath = dstPath + "\\" + subPath[subPath.length - 1] + "\\";
            System.out.println("newdstPath ===========  " +  newdstPath);
            FileStatus[] status =  fs.listStatus(path);
            if(null != status){
                for(FileStatus s : status){
                    // 下载子目录文件
                    downloadFile(s.getPath().toString(),newdstPath);
                }
            }
        }
    }

    /**
     *  上传文件或目录到hdfs
     * @param hdfsPath
     * @param localPath
     * @throws IOException
     */
    public static void uploadFileToHdfs(String hdfsPath, String localPath) throws IOException {
        Configuration conf = new Configuration();
        File file = new File(localPath);
        if(file.isDirectory()){
            uploadDirectory(hdfsPath,localPath,conf);
        }else{
            uploadFile(hdfsPath,localPath,conf);
        }
    }

    /**
     * 上传本地文件到hdfs中
     * @param hdfsPath  hdfs 的路径
     * @param localPath 本地文件路径
     * @param conf
     */
    public static void uploadFile(String hdfsPath, String localPath, Configuration conf) throws IOException {
        File file = new File(localPath);
        // 加上要导入的文件名
        if(hdfsPath.endsWith("/")){
            hdfsPath += file.getName();
        }else{
            hdfsPath += "/"+file.getName();
        }
        // 构建hdfs
        Path path = new Path(hdfsPath);
        FileSystem fs = path.getFileSystem(conf); // FileSystem.get(conf);
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        OutputStream out =  fs.create(path);
        IOUtils.copyBytes(in,out,4096,true);
        in.close();
    }

    /**
     * 上传本地目录到hdfs
     * @param hdfsPath
     * @param localPath
     * @param conf
     * @throws IOException
     */
    public static void uploadDirectory(String hdfsPath,String localPath,Configuration conf) throws IOException {
        Path path = new Path(hdfsPath);
        FileSystem fs  = path.getFileSystem(conf);
        // 如果hdfs目录不存在
        if(!fs.exists(path)){
            fs.mkdirs(path);
        }
        File file = new File(localPath);
        File[] files = file.listFiles();
        for(File f : files){
            if(f.isDirectory()){
                String fname = f.getName();
                if(hdfsPath.endsWith("/")){
                    uploadDirectory(hdfsPath+fname+"/",f.getPath(),conf);
                }else{
                    uploadDirectory(hdfsPath+"/"+fname+"/",f.getPath(),conf);
                }
            }else {
                uploadFile(hdfsPath,f.getPath(),conf);
            }
        }
    }

    /**
     *  文件重命名 ( 文件或目录都可以）
     * @param oldName
     * @param newName
     * @throws IOException
     */
    public static void renameFile(String oldName,String newName) throws IOException {
        // 读取配置文件
        Configuration conf = new Configuration();
        // 获取文件系统
        FileSystem fs = FileSystem.get(conf);
        Path oldPath = new Path(oldName);
        Path newPath = new Path(newName);

        if(!fs.exists(oldPath)){
            System.out.println(oldName + " is not exists in HDFS!");
        }else{
            fs.rename(oldPath,newPath);
        }
    }


}
