import java.sql.*;


class AlbumTest {

    @org.junit.jupiter.api.Test
    void fromSQL() {
        Connection connection = null;
        try {
            //connection = DriverManager.getConnection("jdbc:sqlite:music.db");
            connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/1998p/Desktop/sqlite-tools/music.db");

            System.out.println("Connection created");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select * from albums");
            while (rs.next()) {
                System.out.println("albumId = " + rs.getString("albumId"));
                System.out.println("albumName = " + rs.getString("albumName"));
                System.out.println("artistId = " + rs.getString("artistID"));
                System.out.println("artistName = " + rs.getString("artistName"));
                System.out.println("albumCount = " + rs.getString("albumCount"));
                System.out.println("trackCount = " + rs.getString("trackCount")+"\n");
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