package com.briup.java.Project.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: yfl
 * \* Date: 2019/8/29
 * \* To change this template use File | Settings | File Templates.
 * \* Description:读取配置文件
 * \
 */
public class ServletFinder {
    private static Properties prop;

    //public  ProperFinder(){
    static {
        prop = new Properties();
        try {
            prop.load(new FileInputStream(
                    "E:\\BD1904\\src\\com\\briup\\java\\Project\\conf\\servlet.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getServlet(String key){
        String value = (String)prop.get(key);
        return value;
    }
}