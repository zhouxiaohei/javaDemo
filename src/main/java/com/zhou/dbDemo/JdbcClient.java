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
 * @Description  JDBC工具类
 * @Date 2019/4/8 11:22
 * @Param
 * @return
 **/
@Slf4j
public class JdbcClient {

    private Connection connection;

    private String driver;
    private String url;
    private String userName;
    private String password;

    public JdbcClient(String driver, String url, String userName, String password) {
        this.driver = driver;
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    public static boolean testConnection(String driver, String url, String userName, String password) {
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, userName, password);
            if (connection != null) {
                return true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
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

    public void closeConnection(){
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayNode query(String query, Boolean isCloseConnection){
        if(isCloseConnection){
            return queryAndClose(query);
        }else{
            return query(query);
        }
    }

    public ArrayNode queryAndClose(String query){
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayNode result = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            result = toJsonStr(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(resultSet, null, preparedStatement, connection);
        }
        return result;
    }

    public ArrayNode query(String query){
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayNode result = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
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
        while(resultSet.next()){
            ObjectNode objectNode = objectMapper.createObjectNode();
            for(int i = 1; i <= columnCount; i++){
                String columnName  = metaData.getColumnLabel(i);
                String value = resultSet.getString(columnName);
                objectNode.put(columnName, value);
            }
            arrayNode.add(objectNode);
        }

        return arrayNode;
    }

    public static void main(String[] args) {

//        JdbcClient sqlServerClient = new JdbcClient("com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://10.201.83.214:1433;databaseName=phd", "sa", "1qaz@WSX");
//        JdbcClient postgreClient = new JdbcClient("org.postgresql.Driver", "jdbc:postgresql://10.201.82.69:5432/compaas-V1", "postgres", "postgres");
//        System.out.println(sqlServerClient.query("SELECT * FROM dbo.GetLastValue('ZG010001FI_146,ZG010001DL_130,ZG010001FI_101,ZG010001FI_136','2018-11-28 00:00:00')", true));
//        System.out.println(postgreClient.query("select * from device limit 1", true));

    }

}
