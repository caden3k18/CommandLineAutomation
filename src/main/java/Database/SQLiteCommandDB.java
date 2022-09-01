
/**
 * Haven't tested or run anything yet, just marking placeholders and stuff to deal with later
 * TODO: Testing regex... I can't recall off hand if SQLite3 supports it.
 * TODO: Move the massive switch statement from CommandLineExecutor into the db
 * TODO: Modify command structure to utilize db for the calls and add a description service.
 */

package Database;

import java.sql.*;

public class SQLiteCommandDB {


    public Connection connect(){
        Connection conn = null;

        try{

            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:command.db");
            conn.setAutoCommit(false);

        } catch (SQLException | ClassNotFoundException e){
            System.err.println(e.getMessage());
        }
        return conn;

    }

    public void disconnect(Connection cnn){
        try {
            if (cnn != null)
                cnn.close();
        } catch (SQLException e) {
            // connection close failed.
            System.err.println(e.getMessage());
        }
    }
    public void createTbl(Connection cnn) {

        try {
            // create a database connection
            Statement statement = cnn.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            statement.executeUpdate("drop table if exists commands;");
            statement.executeUpdate("create table commands (id INTEGER PRIMARY KEY, tagName TEXT NOT NULL, varTypes TEXT, translation TEXT, isScheduled TEXT, startTime TEXT, example TEXT, description TEXT, UNIQUE(tagName,varTypes));");
            statement.executeUpdate("CREATE INDEX idx_fileName ON commands (tagName);");
            statement.executeUpdate("CREATE INDEX idx_fileExt ON commands (varTypes);");
            statement.executeUpdate("CREATE INDEX idx_modified ON commands (translation);");
            statement.executeUpdate("CREATE INDEX idx_created ON commands (isScheduled);");
            statement.executeUpdate("CREATE INDEX idx_fileSize ON commands (startTime);");
            statement.close();
            cnn.commit();


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }


    public void insertCommand(Connection cnn, String tagName, String varTypes, String translation, String isScheduled, String startTime, String example, String description) {

        try {


            PreparedStatement pstmt = cnn.prepareStatement("insert or ignore into commands (tagName, varTypes, translation, isScheduled, startTime, example, description) values (?,?,?,?,?,?,?)");
            pstmt.setString(1, tagName);
            pstmt.setString(2, varTypes);
            pstmt.setString(3, translation);
            pstmt.setString(4, isScheduled);
            pstmt.setString(5, startTime);
            pstmt.setString(6, example);
            pstmt.setString(7, description);

            pstmt.executeUpdate();
            pstmt.close();
            cnn.commit();


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

    public String translateCommand(Connection cnn, String tagName) {

        String result = "";
        try {
            // create a database connection
            Class.forName("org.sqlite.JDBC");
            Statement statement = cnn.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select * from commands where tagName LIKE '" + tagName + "' OR description LIKE '%" + tagName + "%'");
            while (rs.next()) {
                // read the result set
                result =  rs.getString("translation");
            }
                statement.close();

        } catch (SQLException | ClassNotFoundException e) {

            System.err.println(e.getMessage());
        }

        return result;

    }


    public String searchCommand(Connection cnn, String search) {

        String result = "";
        try {
            // create a database connection
            Class.forName("org.sqlite.JDBC");
            Statement statement = cnn.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select * from commands where tagName LIKE '%" + search + "%' OR description LIKE '%" + search + "%'");
            while (rs.next()) {
                // read the result set
                result = result + rs.getString("tagName") + " Description: " + rs.getString("description") + " Example: " + rs.getString("example") + "\n";
            }
            statement.close();

        } catch (SQLException | ClassNotFoundException e) {

            System.err.println(e.getMessage());
        }

        return result;

    }

    public String listCommands(Connection cnn) {

        String result = "";
        try {
            // create a database connection
            Class.forName("org.sqlite.JDBC");
            Statement statement = cnn.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select * from commands");
            while (rs.next()) {
                // read the result set
                result = result + rs.getString("tagName") + " Description: " + rs.getString("description") + " Example: " + rs.getString("example") + "\n";
            }
            statement.close();

        } catch (SQLException | ClassNotFoundException e) {

            System.err.println(e.getMessage());
        }

        return result;

    }




}