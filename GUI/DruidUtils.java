package GUI;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
public class DruidUtils {
    private static DataSource datasource;
    static {
        try(InputStream inputStream=DruidUtils.class.getClassLoader().getResourceAsStream("Druid.properties")){
            if(inputStream==null){
                throw new IOException("Druid.properties not found");
            }
            Properties pro=new Properties();
            pro.load(inputStream);
            datasource = DruidDataSourceFactory.createDataSource(pro);

        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static  DataSource getDataSource(){
        return  datasource;
    }
//获取其中一个连接
    public static Connection getConnection(){
        Connection conn=null;
        try{
            conn=datasource.getConnection();
        }catch (SQLException throwAbles){
            throwAbles.printStackTrace();
        }
        return conn;
    }

    //检验连接是否成功
    public static void main(String[] args) throws SQLException {
        Connection conn=DruidUtils.getConnection();
        System.out.println("中国");
        conn.close();
    }
}

