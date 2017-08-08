package com.itheima.utils;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSourceFactory;

public class MyDataSource {  
    private static Properties properties;  
    private static DataSource dataSource;  
    static {  
        try {  
            properties = new Properties();  
            properties.load(MyDataSource.class.getResourceAsStream("/dbcpconfig.properties"));  
            BasicDataSourceFactory b = new BasicDataSourceFactory();  
            dataSource = b.createDataSource(properties);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    public static DataSource getdataSource() {  
        return dataSource;  
    }  
}  
