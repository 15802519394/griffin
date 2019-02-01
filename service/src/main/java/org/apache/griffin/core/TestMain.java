package org.apache.griffin.core;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;

/**
 * @Author:linshuo(170285)
 * @Description
 * @Date：19:24 2019/1/10
 * @Modified By :
 * @Company : ZLST
 */
public class TestMain {

    public static void main( String[] args )
    {
        //System.setProperty("hadoop.home.dir", "E:\\prgramme\\hadoop-2.7.1");
        //System.setProperty("HADOOP_USER_NAME", "root");
        Configuration conf = new Configuration();
        FileSystem fs = null;
        try {
            fs = FileSystem.get(URI.create("hdfs://192.168.20.121:9000/griffin"), conf);
            Path path = new Path("3.txt");
            FSDataOutputStream out = fs.create(path);   //创建文件
            out.write("hello".getBytes("UTF-8"));
            out.writeUTF("da jia hao,cai shi zhen de hao!");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
