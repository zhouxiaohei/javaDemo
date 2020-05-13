package com.zhou.dbDemo.dataSource;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class SaveDataByDbcp {
	private final Logger log = Logger.getLogger(SaveDataByDbcp.class);
	
	public void saveUrls(List<String> urlList) throws SQLException{
		Connection connection = DataSourceDbcpDemo.getConnection();
		connection.setAutoCommit(false);
		int count = 0;
		String insertSql = "insert into website_info(url,domain) values(?,?)";
		PreparedStatement ps =  (PreparedStatement)connection.prepareStatement(insertSql);;
		for (String url : urlList) {
			count++;
			String domain = url;
			if (!url.startsWith("http://") && !url.startsWith("https://")) {
				domain = "http://" + url;
			}
			ps.setObject(1, url);
			ps.setObject(2, domain);
			ps.addBatch();
			if (count % 10000 == 0) {
				ps.executeBatch();
				connection.commit();
				log.info("入库url数量：" + count);
			}
		}

		ps.executeBatch();
		connection.commit();
		DataSourceDbcpDemo.close(connection);
		log.info("入库url数量：" + count);

	}

	/**
	 * @描述: 更新状态和完成时间
	 * @说明:
	 * @修改时间: 2017年1月20日 下午11:46:34
	 * @param status
	 * @param domain
	 */
	public void updateStatus(int status, String domain){
		Connection connection = DataSourceDbcpDemo.getConnection();
		String updateSql = "update website_info set status = ?, time = ? where domain = ?";
		
		System.out.println("保存开始-----status:" + status  + "----" + domain);
		try {
			PreparedStatement ps = (PreparedStatement)connection.prepareStatement(updateSql);
			ps.setObject(1, status);
			ps.setObject(2, new Date());
			ps.setObject(3, domain);
			
			ps.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			log.error("更新url状态时异常" + domain, e);
		}finally{
			DataSourceDbcpDemo.close(connection);
		}
		
		System.out.println("保存结束-----status:" + status  + "----" + domain);
		
	}
	
	/**
	 * 更新爬取内容
	 * @描述:
	 * @说明:
	 * @修改时间: 2017年1月20日 下午11:46:50
	 */
	public void updateContent(String title, String keywords, String description, String content, String links, String domain) {
		System.out.println("保存内容-----" + domain);
		Connection connection = DataSourceDbcpDemo.getConnection();
		String updateSql = "update website_info set title = ?, keyword = ?, description =?, content =?, links = ? where domain = ?";

		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(updateSql);
			ps.setObject(1, title);
			ps.setObject(2, keywords);
			ps.setObject(3, description);
			ps.setObject(4, content);
			ps.setObject(5, links);
			ps.setObject(6, domain);

			ps.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			log.error("更新爬取内容时异常异常" + domain, e);
		}finally{
			DataSourceDbcpDemo.close(connection);
		}
	}

	public static void main(String[] args) {
/*		Connection connection = DataSourceHelp.getConnection();
		String sql = "insert into website_info(url,domain) values(1,2)";

		Statement createStatement = connection.createStatement();
		createStatement.execute(sql);
		connection.commit();*/
		
//		String filePath = "C:\\Users\\sks\\Desktop\\10000_url.txt";
//		List<String> urlList = RedisTest.readFileContent(filePath);
//		try {
//			new SaveDataUtils().saveUrls(urlList);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		//new SaveDataUtils().updateContent(null,null,"22000",null,null,"http://mobileserver.seedonk.com.cn");
	}

}
