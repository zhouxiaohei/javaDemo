package com.zhou.studyDemo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 *  枚举用来定义常量、单例等。
 * <p>说明: enumDemo</p>
 * <p>类名: enumDemo</p>
 * <p>修改时间: 2017年5月26日 下午3:09:36</p>
 */
public class enumDemo {
	
	public static void main(String[] args) {
		//System.out.println(TrafficLamp.RED);
	}
	
    public enum Color {  
    	 RED("红色", 1), GREEN("绿色", 2), BLANK("白色", 3), YELLO("黄色", 4); 
    	   // 成员变量  
    	    private String name;  
    	    private int index;  
    	    private Color(String name, int index) {  
    	        this.name = name;  
    	        this.index = index;  
    	    }
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public int getIndex() {
				return index;
			}
			public void setIndex(int index) {
				this.index = index;
			}
    	    
      }
    public enum MyDataBaseSource {
        DATASOURCE;
        private ComboPooledDataSource cpds = null;

        private MyDataBaseSource() {
            try {

                /*--------获取properties文件内容------------*/
                // 方法一:
                /*
                 * InputStream is =
                 * MyDBSource.class.getClassLoader().getResourceAsStream("jdbc.properties");
                 * Properties p = new Properties(); p.load(is);
                 * System.out.println(p.getProperty("driverClass") );
                 */

                // 方法二：(不需要properties的后缀)
                /*
                 * ResourceBundle rb = PropertyResourceBundle.getBundle("jdbc") ;
                 * System.out.println(rb.getString("driverClass"));
                 */

                // 方法三：(不需要properties的后缀)
                ResourceBundle rs = ResourceBundle.getBundle("jdbc");
                cpds = new ComboPooledDataSource();
                cpds.setDriverClass(rs.getString("driverClass"));
                cpds.setJdbcUrl(rs.getString("jdbcUrl"));
                cpds.setUser(rs.getString("user"));
                cpds.setPassword(rs.getString("password"));
                cpds.setMaxPoolSize(Integer.parseInt(rs.getString("maxPoolSize")));
                cpds.setMinPoolSize(Integer.parseInt(rs.getString("minPoolSize")));
                System.out.println("-----调用了构造方法------");
                ;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Connection getConnection() {
            try {
                return cpds.getConnection();
            } catch (SQLException e) {
                return null;
            }
        }

    }
}
