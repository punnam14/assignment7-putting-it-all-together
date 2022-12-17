import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Album extends Entity {
    protected ArrayList<Song> songs;
    protected Artist artist;
    protected Album album;

    /**
     * takes in a parameter name and assigns it to name
     * @param name
     */
    public Album(String name) {
        super(name);
    }

    /**
     * used to fetch the name of the album
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * used to compare two album objects wherever required
     * @return boolean
     */
    public boolean equals(Album otherAlbum) {
        if ((this.artist.equals(otherAlbum.getArtist())) &&
                (this.name.equals(otherAlbum.getName()))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * returns a array list of songs
     * @return Songs
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
     * used to get the artist
     */
    public Artist getArtist() {
        return artist;
    }

    /**
     * used to get the artist
     */
    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    /**
     * method for writing into SQL
     */
    public String toSQL() {
        return "insert into Album (id, name, nSongs, artist) values (" +
                this.entityID + ", '" + this.name + "', " +
                this.songs.size() + ", " +
                this.artist.entityID  + ");";
    }

    /**
     * @NotNull
     * @param rs
     * method for retrieving data from SQL
     */
    public void fromSQLAlbum(ResultSet rs) {
        try {
            this.entityID = rs.getInt("id");
            this.name = rs.getString("name");
            this.name = rs.getString("artistID");
            this.name = rs.getString("artistName");
        } catch (SQLException e) {
            System.out.println("SQL Exception" + e.getMessage());
        }

    }

}
