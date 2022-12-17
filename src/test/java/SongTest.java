import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class SongTest {

    @Test
    void fromSQL() {
        Connection connection = null;
        try {
            //connection = DriverManager.getConnection("jdbc:sqlite:music.db");
            connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/1998p/Desktop/sqlite-tools/music.db");

            System.out.println("Connection created");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select * from songs");
            while (rs.next()) {
                System.out.println("ArtistID = " + rs.getInt("artistID"));
                System.out.println("ArtistName = " + rs.getString("artistName"));
                System.out.println("TrackName = " + rs.getInt("trackName"));
                System.out.println("TrackID = " + rs.getInt("trackID"));
                System.out.println("AlbumName = " + rs.getInt("albumName"));
                System.out.println("AlbumID = " + rs.getInt("albumID")+"\n");
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