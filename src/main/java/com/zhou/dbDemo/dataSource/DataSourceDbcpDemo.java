package com.zhou.dbDemo.dataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DBCP配置类  数据库连接池
 */
public class DataSourceDbcpDemo {
	
    private static Properties properties = new Properties();
    private static DataSource dataSource;
    //加载DBCP配置文件
    static{
    	InputStream fis = DataSourceDbcpDemo.class.getClassLoader().getResourceAsStream("dbcp.properties");
        try{
        	properties.load(fis);
            dataSource = BasicDataSourceFactory.createDataSource(properties);
        }catch(Exception e){
        	System.out.println("加载数据库报错。。。" + e);
        }
    }
    
    //从连接池中获取一个连接
    public static Connection getConnection(){
        Connection connection = null;
        try{
            connection = dataSource.getConnection();
        }catch(SQLException e){
            e.printStackTrace();
        }
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    
    public static void close(Connection connection){
    	try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    public static void main(String[] args) {
        //System.out.println(getConnection());
    }
}