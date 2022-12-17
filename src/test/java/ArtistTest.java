import org.junit.jupiter.api.Test;

import java.sql.*;

class ArtistTest {

    @Test
    void fromSQL() {
        Connection connection = null;
        try {
            //connection = DriverManager.getConnection("jdbc:sqlite:music.db");
            connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/1998p/Desktop/sqlite-tools/music.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select * from artists");
            while (rs.next()) {
                System.out.println("ArtistID = " + rs.getInt("artistID"));
                System.out.println("ArtistName = " + rs.getString("artistName"));
                System.out.println("ArtistGenre = " + rs.getInt("artistGenre"));
                System.out.println("ArtistMood = " + rs.getString("artistMood"));
                System.out.print("\n");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}