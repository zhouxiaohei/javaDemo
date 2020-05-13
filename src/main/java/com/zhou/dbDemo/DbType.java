package com.zhou.dbDemo;

/**
  * @Author JackZhou
  * @Description  数据库类型枚举类
  * @Date 2019/4/8 13:57
  * @Param 
  * @return 
 **/
public enum DbType {

    SQLSERVER("SQLSERVER", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    POSTGRESQL("POSTGRESQL", "org.postgresql.Driver"),
    MYSQL("MYSQL", "com.mysql.jdbc.Driver"),
    ORACLE("ORACLE", "oracle.jdbc.driver.OracleDriver");

    private String name;
    private String driver;

    DbType(String name, String driver) {
        this.name = name;
        this.driver = driver;
    }

    public static String getDriver(String name){
        for(DbType dbType : DbType.values()){
            if(dbType.getName().equals(name)){
                return dbType.getDriver();
            }
        }
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public static void main(String[] args) {

      //System.out.println( getDriver("POSTGRESQL") );
    }
}
