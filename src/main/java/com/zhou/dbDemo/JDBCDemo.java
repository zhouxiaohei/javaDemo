package com.zhou.dbDemo;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import com.zhou.util.PropertiesUtils;
import org.apache.log4j.Logger;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * JDBC数据库连接类
 */
public class JDBCDemo {
	private final Logger log = Logger.getLogger(JDBCDemo.class);
	private String driver = "com.mysql.jdbc.Driver";
	private Connection con2;

	public void init() {
		Properties props = PropertiesUtils.readProperties("jdbc.properties");
		String url = props.getProperty("jdbc.url");
		String username = props.getProperty("jdbc.username");
		String password = props.getProperty("jdbc.password");

		try {
			Class.forName(driver);
			con2 = (Connection) DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			log.info("连接目标数据库失败", e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Connection getCon2() {
		return con2;
	}

	public void close(Connection con1, Statement st1, ResultSet rs1, Connection con2, Statement st2, ResultSet rs2, PreparedStatement ps) {
		if (null != rs1) {
			try {
				rs1.close();
				if (null != rs2) {
					rs2.close();
				}
				if (null != ps) {
					ps.close();
				}
			} catch (SQLException e) {
				log.info("关闭ResultSet出错", e);
			}
		}

		if (null != st1) {
			try {
				st1.close();
				if (null != st2) {
					st2.close();
				}
			} catch (SQLException e) {
				log.info("关闭Statement出错", e);
			}
		}
		if (null != con1) {
			try {
				con1.close();
				if (null != con2) {
					con2.close();
				}
			} catch (SQLException e) {
				log.info("关闭Connection出错", e);
			}
		}
	}

	public static void main(String[] args) {
		new JDBCDemo().init();
	}

}
