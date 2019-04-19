// Code based on tutorial code at this site: http://zetcode.com/db/mysqljava/
// modified to user log4j.  Java and build.gradle found here: https://github.com/hortonuva/jdbc-demo-0.git

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;


public class JdbcDemo0 {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {

        Configurator.setLevel(logger.getName(), Level.TRACE);
        logger.info("Entering application.");

        // see notes at end about creating database and user
        String url = "jdbc:mysql://localhost:3306/sde_demo0?useSSL=false";
        String user = "horton@localhost";
        String password = "mypass";

        // demo using a very simple SQL command
        String query = "SELECT VERSION()";

        // if you don't know try-with-resources, see http://tutorials.jenkov.com/java-exception-handling/try-with-resources.html
        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            if (rs.next()) {
                System.out.println("Output of query is: " + rs.getString(1));
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            // see what happens if you edit code above to give bad user name, bad password, bad database, etc.
        }
        logger.info("Exiting application.");
    }
}

/*

Before running this program, I created a database and user as follows:

$ mysql -u root -p
mysql> create database sde_demo0;
mysql> create user 'horton@localhost' identified by 'mypass';
mysql> grant all privileges on * . * to 'horton@localhost';
mysql> grant all on * . * to 'horton@localhost' with grant option;
mysql> flush privileges;


*/