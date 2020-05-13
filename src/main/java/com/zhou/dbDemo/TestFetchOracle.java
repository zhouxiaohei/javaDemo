package com.zhou.dbDemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Author JackZhou
 * @Date 2019/9/19  16:18
 **/

@Slf4j
public class TestFetchOracle {

    private Connection connection;

    private String driver;
    private String url;
    private String userName;
    private String password;

    public TestFetchOracle(String driver, String url, String userName, String password) {
        this.driver = driver;
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    private static void close(ResultSet rs, Statement st, PreparedStatement ps, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private synchronized Connection getConnection(){
        try {
            if(connection == null || connection.isClosed()){
                Class.forName(driver);
                connection = DriverManager.getConnection(url, userName, password);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public ArrayNode query(String query){
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayNode result = null;
        try {
            //preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setFetchSize(1000);
            //preparedStatement.setFetchDirection(ResultSet.FETCH_REVERSE);
            resultSet = preparedStatement.executeQuery();
             //preparedStatement.executeUpdate();
            result = toJsonStr(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(resultSet, null, preparedStatement, null);
        }
        log.info("查找sql [{}] {} 查询结果 {}", query, System.getProperty("line.separator"), result);
        return result;
    }

    public static ArrayNode toJsonStr(ResultSet resultSet) throws SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = objectMapper.createArrayNode();

        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        int count = 0;
        int sum = 0;
        while(resultSet.next()){
            count++;
            ObjectNode objectNode = objectMapper.createObjectNode();
            for(int i = 1; i <= columnCount; i++){
                String columnName  = metaData.getColumnLabel(i);
                String value = resultSet.getString(columnName);
                objectNode.put(columnName, value);
            }
            arrayNode.add(objectNode);
            if(count % 100 == 0){
                sum++;
                log.info("100个结果集 {}", sum);
                arrayNode = objectMapper.createArrayNode();
            }
        }
        return arrayNode;
    }

    /**
     host:10.200.60.49
     port:1521
     servicename:helowin
     username:oracle
     password:123456
     **/
    public static void main(String[] args) {
        TestFetchOracle oracleClient = new TestFetchOracle(DbType.getDriver("ORACLE") , "jdbc:oracle:thin:@//10.200.60.49:1521/helowin", "oracle", "123456");

        /**
           setFetchSize 有用，可提高查询速度，如果设置太大会内存溢出，默认提取size好像是10条
         **/
        //ArrayNode query = oracleClient.query("select SYSDATE from dual");
//        ArrayNode create = oracleClient.query("create table event_test (id NVARCHAR2(31), body NCLOB, entity_id NVARCHAR2(31), entity_type NVARCHAR2(255), "
//         + "event_type  NVARCHAR2(255), event_uid NVARCHAR2(255)，tenant_id NVARCHAR2(31))");
        ArrayNode query = oracleClient.query("select * from event_test");
        log.info("打印下行数：" + query.size());
    }
}
