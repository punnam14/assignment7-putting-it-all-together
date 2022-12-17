import java.sql.*;
import java.util.ArrayList;

public class RetriveData {

    /**
     * method for retrieving Album table data from the SQL database
     */
    public static void fromSQLAlbums(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:music.db");
            //connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/1998p/Desktop/sqlite-tools/music.db");

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("select * from albums");
            while (rs.next()) {
                System.out.println("Album Number: " + rs.getString("albumId"));
                System.out.println("Album Name: " + rs.getString("albumName"));
                System.out.println("Artist Number: " + rs.getString("artistID"));
                System.out.println("Artist Name: " + rs.getString("artistName"));
                System.out.println("Number of Albums: " + rs.getString("albumCount"));
                System.out.println("Number of Songs: " + rs.getString("trackCount")+"\n");
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

    /**
     * method for retrieving Artist table data from the SQL database
     */
    public static void fromSQLArtists() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:music.db");
            //connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/1998p/Desktop/sqlite-tools/music.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("select * from artists");
            while (rs.next()) {
                System.out.println("Artist Number: " + rs.getString("artistID"));
                System.out.println("Artist Name: " + rs.getString("artistName"));
                System.out.println("Artist Genre: " + rs.getString("artistGenre"));
                System.out.println("Artist Mood: " + rs.getString("artistMood"));
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

    /**
     * method for retrieving Songs table data from the SQL database
     */
    public static void fromSQLSongs() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:music.db");
            //connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/1998p/Desktop/sqlite-tools/music.db");

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("select * from songs");
            while (rs.next()) {
                System.out.println("Song Number: " + rs.getString("trackID"));
                System.out.println("Artist Number: " + rs.getString("artistID"));
                System.out.println("Artist Name: " + rs.getString("artistName"));
                System.out.println("Song Name: " + rs.getString("trackName"));
                System.out.println("Song Genre: " + rs.getString("genreTrack"));
                System.out.println("Album Name: " + rs.getString("albumName"));
                System.out.println("Album Number: " + rs.getString("albumID")+"\n");
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

    /**
     * method for retrieving a playlist of the top 5 pop-rock songs from the Songs table
     */
    public static void fromSQLSongsPlaylist() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:music.db");
            //connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/1998p/Desktop/sqlite-tools/music.db");

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("select * from songs where genreTrack = \"Pop-Rock\"");
            while (rs.next()) {
                System.out.println("Song Number: " + rs.getString("trackID"));
                System.out.println("Artist Number: " + rs.getString("artistID"));
                System.out.println("Artist Name: " + rs.getString("artistName"));
                System.out.println("Song Name: " + rs.getString("trackName"));
                System.out.println("Song Genre: " + rs.getString("genreTrack"));
                System.out.println("Album Name: " + rs.getString("albumName"));
                System.out.println("Album Number: " + rs.getString("albumID")+"\n");
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

    /**
     * method for converting the playlist to an XML file
     */
    public void fromSQLSongsPlaylistXML() {
        Library library = new Library();
        Song s1 = new Song();
        Song s2 = new Song();
        Song s3 = new Song();
        Song s4 = new Song();
        Song s5 = new Song();
        ArrayList<String> songNames = new ArrayList<String>();
        ArrayList<String> artistNames = new ArrayList<String>();
        ArrayList<String> albumNames = new ArrayList<String>();

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:music.db");
            //connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/1998p/Desktop/sqlite-tools/music.db");

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("select * from songs where genreTrack = \"Pop-Rock\"");
            while (rs.next()) {
                String artistName = rs.getString("artistName");
                artistNames.add(artistName);
                String track = rs.getString("trackName");
                songNames.add(track);
                String albumName = rs.getString("albumName");
                albumNames.add(albumName);
            }
            s1.setName(songNames.get(0));
            s1.setPerformer(new Artist(artistNames.get(0)));
            s1.setAlbum(new Album(albumNames.get(0)));
            s2.setName(songNames.get(1));
            s2.setPerformer(new Artist(artistNames.get(1)));
            s2.setAlbum(new Album(albumNames.get(1)));
            s3.setName(songNames.get(2));
            s3.setPerformer(new Artist(artistNames.get(2)));
            s3.setAlbum(new Album(albumNames.get(2)));
            s4.setName(songNames.get(3));
            s4.setPerformer(new Artist(artistNames.get(3)));
            s4.setAlbum(new Album(albumNames.get(3)));
            s5.setName(songNames.get(4));
            s5.setPerformer(new Artist(artistNames.get(4)));
            s5.setAlbum(new Album(albumNames.get(4)));
            Library.addSong(s1);
            Library.addSong(s2);
            Library.addSong(s3);
            Library.addSong(s4);
            Library.toXML();
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
