import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Artist extends Entity {

    protected Artist performer;
    protected ArrayList<Song> songs;
    protected ArrayList<Album> albums;

    /**
     * takes in a parameter name and assigns it to name
     * @param name
     */
    public Artist(String name) {
        super(name);
    }

    /**
     * to fetch the list of songs
     * @return an arraylist of songs
     */
    protected ArrayList<Song> getSongs() {
        return songs;
    }

    /**
     * used to set songs
     */
    protected void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    /**
     * used to get list of albums
     * @return an arraylist of songs
     */
    protected ArrayList<Album> getAlbums() {
        return albums;
    }

    /**
     * used to set albums
     */
    protected void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }

    /**
     * @param s
     * used to add songs
     */
    public void addSong(Song s) {
        songs.add(s);
    }

    /**
     * method for writing into SQL
     */
    public String toSQL() {
        return "insert into artist (id, nAlbums, name, nSongs) values (" +
                this.entityID + ", " + this.albums.size() +
                ", '" + this.name + "', " +
                this.songs.size()  + ");";
    }

    /**
     * @NotNull
     * @param rs
     * method for retrieving data from SQL
     */
    public void fromSQLArtist(ResultSet rs) {
        try {
            this.entityID = rs.getInt("id");
            this.name = rs.getString("name");
        } catch (SQLException e) {
            System.out.println("SQL Exception" + e.getMessage());
        }

    }

}
