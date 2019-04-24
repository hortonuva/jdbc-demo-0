// Code based on tutorial code at this site: http://zetcode.com/db/mysqljava/
// modified to user log4j.  Java and build.gradle found here: https://github.com/hortonuva/jdbc-demo-0.git

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.awt.*;
import java.sql.*;


public class JdbcDemo1 {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {

        Configurator.setLevel(logger.getName(), Level.TRACE);
        logger.info("Entering application.");

        // see notes at end about creating database and user
        String url = "jdbc:mysql://localhost:3306/sde_demo0?useSSL=false";
        String user = "horton@localhost";
        String password = "mypass";

        try {  // note: not using try-with-resources, so I really should be calling close() explicitly later
            Connection con = DriverManager.getConnection(url, user, password);

            // JDBC code to insert a new course
            PreparedStatement insertStmt = con
                    .prepareStatement("insert into course VALUES (?,?,?,?)");
            insertStmt.setString(1, "CS1110");
            insertStmt.setString(2, "Intro. to Programming");
            insertStmt.setString(3, "Brunelle");
            insertStmt.setInt(4,200);
            insertStmt.executeUpdate();
            logger.info("Completed INSERT.");

            // JDBC code to update values a existing row in table
            PreparedStatement updateStmt = con
                    .prepareStatement("update course SET capacity = ? "
                            + "WHERE id = ?");
            updateStmt.setInt(1, 300);
            updateStmt.setString(2, "CS2110");
            updateStmt.executeUpdate();
            logger.info("Completed UPDATE.");

            // demo done in class to show SELECT and ResultSet
            PreparedStatement queryStmt = con.prepareStatement("SELECT * from course WHERE capacity > ?");
            queryStmt.setInt(1, 100);
            ResultSet rs = queryStmt.executeQuery();
            ResultSetMetaData rsMeta = rs.getMetaData();
            int colCount = rsMeta.getColumnCount();

            while (rs.next()) {
                // use loop and colCount to get each column as an Object (shown in class)
                for (int i=1; i <= colCount; ++i) {
                    System.out.printf("%-22s ", rs.getObject(i));
                }
                System.out.println();
                // alternative to loop above: grab each column, if you know how many and what their types are
//                System.out.printf("%s:%s:%s:%d\n", rs.getString(1), rs.getString(2).trim(),
//                        rs.getString(3).trim(), rs.getInt(4));
            }
            logger.info("Completed SELECT.");

        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            // see what happens if you edit code above to give bad user name, bad password, bad database, etc.
        }
        logger.info("Exiting application.");
    }
}

