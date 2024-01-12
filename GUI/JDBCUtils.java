package GUI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtils {
    static String url="jdbc:mysql://localhost:3306";

    static   String user="root";

    static  String password="1234";

    public static Connection getConnection() {
        Connection conn=null;

        {
            try {
                conn = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return conn;
    }

    public static void main(String[] args) throws SQLException {
        try(Connection connection=JDBCUtils.getConnection()){
            System.out.println("中国");
        }
//        System.out.println("hello");
    }
}
