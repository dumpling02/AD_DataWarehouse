package com.atguigu.ad.hive.udf;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ConstantObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.IOUtils;
import org.lionsoul.ip2region.xdb.Searcher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ParseIP extends GenericUDF {

    //定义IP定位查找器
    private Searcher searcher;

    /**
     * 检查udf函数的输入输出的数据类型
     * @param arguments 函数的输入参数  arguments[0]为ip2region数据库的存储路径 arguments[1]为等待解析的ip
     * @return 返回值的输出类型
     */
    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {

        //校验参数个数
        if (arguments.length != 2) {
            throw new UDFArgumentException("parse_ip函数需要接受两个参数");
        }

        //检查第1个参数是否是STRING类型
        ObjectInspector hdfsPathOI = arguments[0];
        if (hdfsPathOI.getCategory() != ObjectInspector.Category.PRIMITIVE) {
            throw new UDFArgumentException("parse_ip函数的第1个参数应为基本数据类型");
        }
        PrimitiveObjectInspector primitiveHttpURLOI = (PrimitiveObjectInspector) hdfsPathOI;
        if (PrimitiveObjectInspector.PrimitiveCategory.STRING != primitiveHttpURLOI.getPrimitiveCategory()) {
            throw new UDFArgumentException("parse_ip函数的第1个参数应为STRING类型");
        }

        //构造Searcher对象
        // 判断hdfsPathOI是否为一个常量Constant （instanceof yyy 属于 xxx）
        if (hdfsPathOI instanceof ConstantObjectInspector) {

            //获取第一个参数(HDFS路径)的值(常量): arguments[0]
            String filePath = ((ConstantObjectInspector) hdfsPathOI).getWritableConstantValue().toString();
            //创建Path对象
            Path path = new Path(filePath);
            Configuration conf = new Configuration();

            try {
                //获取HDFS文件系统客户端
                FileSystem fs = FileSystem.get(conf);

                //打开文件获取输入流
                FSDataInputStream inputStream = fs.open(path);
                //创建ByteArrayOutputStream(可将内容写入字节数组)
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                //使用Hadoop提供的IOUtils将输入流的全部内容拷贝到输出流
                IOUtils.copyBytes(inputStream, outputStream, 1024);

                // 从输出流得到字节数组
                byte[] buffer = outputStream.toByteArray();

                // 构造基于内存的Searcher
                // 缓存ip解析库
                searcher = Searcher.newWithBuffer(buffer);  // 读取文件内容
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //检查第2个参数是否是STRING类型
        ObjectInspector ipOI = arguments[1];
        if (ipOI.getCategory() != ObjectInspector.Category.PRIMITIVE) {
            throw new UDFArgumentException("parse_ip函数的第2个参数应为基本数据类型");
        }
        PrimitiveObjectInspector primitiveIPOI = (PrimitiveObjectInspector) ipOI;
        if (PrimitiveObjectInspector.PrimitiveCategory.STRING != primitiveIPOI.getPrimitiveCategory()) {
            throw new UDFArgumentException("parse_ip函数的第2个参数应为STRING类型");
        }

        // 检查输出类型
        //构造函数返回值的对象检查器
        ArrayList<String> structFieldNames = new ArrayList<>();
        ArrayList<ObjectInspector> structFieldObjectInspectors = new ArrayList<>();

        // 列名列表
        structFieldNames.add("country");
        structFieldNames.add("area");
        structFieldNames.add("province");
        structFieldNames.add("city");
        structFieldNames.add("isp");

        // 属性名列表
        structFieldObjectInspectors.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        structFieldObjectInspectors.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        structFieldObjectInspectors.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        structFieldObjectInspectors.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        structFieldObjectInspectors.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);

        // 如何检查结构图的返回类型呢？ 其实已经有封装好的方法了
        // getStandardStructObjectInspector(列名，属性名)
        return ObjectInspectorFactory.getStandardStructObjectInspector(structFieldNames, structFieldObjectInspectors);
    }

    /**
     * 对udf函数的输入数据进行加工处理
     * @param arguments udf函数的输入参数
     * @return 返回udf函数的输出结果
     */
    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {

        //获取IP地址
        String ipAddress = arguments[1].get().toString();

        //构造返回值对象,Struct的列名已经在initialize方法中声明,故此处只需返回值即可
        ArrayList<Object> result = new ArrayList<>();
        try {
            // 使用IP解析库
            // 解析ip对应的region
            String region = searcher.search(ipAddress);
            //切分region
            String[] split = region.split("\\|");
            //构造放回值
            // 作为hive udf函数的返回值
            result.add(split[0]);
            result.add(split[1]);
            result.add(split[2]);
            result.add(split[3]);
            result.add(split[4]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 用于在Explain执行计划中展示函数调用信息
     */
    @Override
    public String getDisplayString(String[] children) {
        //用于在Explain执行计划中展示函数调用信息
        return getStandardDisplayString("parse_ip", children);
    }
}
