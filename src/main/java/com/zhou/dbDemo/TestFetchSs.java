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
 * @Date 2019/9/19  11:33
 **/
@Slf4j
public class TestFetchSs {
    private Connection connection;

    private String driver;
    private String url;
    private String userName;
    private String password;

    public TestFetchSs(String driver, String url, String userName, String password) {
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
            preparedStatement = connection.prepareStatement(query);
            //preparedStatement.setFetchSize(1000000);
            resultSet = preparedStatement.executeQuery(); //默认 128
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

    public static void main(String[] args) {
        ArrayNode query = null;

        /**  sqlser   自动释放resultSet   ***/
        TestFetchSs sqlServerClient = new TestFetchSs(DbType.getDriver("SQLSERVER") , "jdbc:sqlserver://10.201.83.214:1433;databaseName=phd", "sa", "1qaz@WSX");
        //query = sqlServerClient.query("SELECT top 10000 * FROM phd_value");
        query = sqlServerClient.query("SELECT * FROM phd_value");


    }
}
