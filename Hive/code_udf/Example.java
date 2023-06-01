package com.atguigu.ad.hive.udf;

import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector.PrimitiveCategory;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorUtils;

public class Example {
    public static void main(ObjectInspector[] args) {


        // 创建一个ObjectInspector来检查字符串类型
        // step1
        // Object类型对象
        ObjectInspector inspector = args[0];
        System.out.println("Object类型: " + inspector.getCategory());

        // step2
        // PrimitiveObject类型对象
        PrimitiveObjectInspector category = (PrimitiveObjectInspector) inspector;

        // step3
        // 打印原始类型
        System.out.println("原始(Primitive)类型: " + category.getPrimitiveCategory());
    }
}

