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
 * @Description
 * @Author JackZhou
 * @Date 2019/9/18  15:33
 **/

@Slf4j
public class TestFetchMysql {

    private Connection connection;

    private String driver;
    private String url;
    private String userName;
    private String password;

    public TestFetchMysql(String driver, String url, String userName, String password) {
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
            preparedStatement.setFetchSize(Integer.MIN_VALUE);
            //preparedStatement.setFetchDirection(ResultSet.FETCH_REVERSE);
            resultSet = preparedStatement.executeQuery();
            printMem();
            result = toJsonStr(resultSet);
            printMem();
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
     totalMemory 虚拟机已经从操作系统拿到的内存
     maxMem 虚拟机可以从操作系统拿到的最大内存
     freeMem  totalMem - 已经使用的内存
     **/
    public static void printMem(){
        log.info("totalMemory:{}, maxMemory:{}, freeMemory:{}", byteToM(Runtime.getRuntime().totalMemory()), byteToM(Runtime.getRuntime().maxMemory()), byteToM(Runtime.getRuntime().freeMemory()));
    }

    /**
     * 把byte转换成M
     * @param bytes
     * @return
     */
    public static long byteToM(long bytes){
        long kb =  (bytes / 1024 / 1024);
        return kb;
    }

    public static void main(String[] args) {

        /** mysql **/
        /**
          网上教程要设置以下两个参数，实际测试不需要  mysql5.7
         connection.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
         preparedStatement.setFetchDirection(ResultSet.FETCH_REVERSE);

         必须设置 preparedStatement.setFetchSize(Integer.MIN_VALUE);  设置setFetchSize =20 不起作用  内存溢出

         **/
        TestFetchMysql mysqlClient = new TestFetchMysql(DbType.getDriver("MYSQL") , "jdbc:mysql://10.200.60.92:3306/test_fetch", "root", "123456");

        ArrayNode query = mysqlClient.query("select * from event");
        log.info("打印下行数：" + query.size());
    }
}
