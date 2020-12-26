import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ResumeSQLHelper {
    
    public void connect(){
        Connection conn = null;
        try{
            System.out.println("Starting Connection to Database..");
            String url = "jdbc:sqlite:src/applications.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Connection established");
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE Applications(id int primary key);";
            stmt.executeUpdate(sql);
            stmt.close();

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
