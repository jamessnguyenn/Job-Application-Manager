import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ResumeSQLHelper {
    private static final String CREATE_SQL= "CREATE TABLE if not exists Applications(job_title text not null, company text not null, description text not null, link text not null, status text not null, date Date primary key not null);";
    private PreparedStatement insertStmt;
    private PreparedStatement updateStmt;
    private Connection conn;
    public ResumeSQLHelper(){
        try{
            System.out.println("Starting Connection to Database..");
            String url = "jdbc:sqlite:src/applications.db";
            conn = DriverManager.getConnection(url);
            createTable();
            insertStmt = conn.prepareStatement("Insert into Applications values(?, ?, ?, ?, 'Pending', DateTime('now', 'localtime'));");
            updateStmt = conn.prepareStatement("Update Applications SET status = ? WHERE date = ?");
            System.out.println("Connection established");
        }catch(SQLException e){
            System.out.println(e.getMessage());
            System.out.println("Connection unsucessful");
        }
    }
    
    private void createTable(){
        try { 
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(CREATE_SQL);
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
       
    }
    public void insertIntoTable(String jobTitle, String company, String description, String link){
       
        try {
            insertStmt.setString(1, jobTitle);
            insertStmt.setString(2,company);
            insertStmt.setString(3, description);
            insertStmt.setString(4, link);
            insertStmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
    }
    public void deleteFromTable(String date){

    }
    public void updateTable(String date, String status){
        try {
            updateStmt.setString(1, status);
            updateStmt.setString(2, date);
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void clearDatabase(String date){

    }
    public ResultSet getData(){
        ResultSet rs = null;
        try{
            PreparedStatement selectStmt = conn.prepareStatement("Select * from Applications ORDER BY date DESC;");
            rs = selectStmt.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }
}
