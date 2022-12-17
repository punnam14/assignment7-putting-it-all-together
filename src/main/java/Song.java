import java.sql.ResultSet;
import java.sql.SQLException;

public class Song extends Entity {
    protected Album album;
    protected Artist performer;
    protected SongInterval duration;
    protected String genre;

    /**
     * a constructor for song
     */
    public Song() {
        album = null;
        performer = null;
        duration = null;
        genre = null;
    }

    /**
     * @param name
     * a constructor for song with name as an argument
     */
    public Song(String name) {
        super(name);
        album = new Album("");
        performer = new Artist("");
        duration = new SongInterval(0);
        genre = "";

    }

    /**
     * @param name,
     * @param artist
     * @param album
     * a constructor for song with name,artist and album as an argument
     */
    public Song(String name, String artist, String album) {
        super(name);
        this.album = new Album(album);
        this.performer = new Artist(artist);
    }

    /**
     * @param name,
     * @param length
     * a constructor for song with name and length as an argument
     */
    public Song(String name, int length) {
        super(name);
        duration = new SongInterval(length);
        genre = "";
    }

    /**
     * used to get list of albums
     * @return an arraylist of songs
     */
    protected Album getAlbum() {
        return album;
    }

    /**
     * used to set an album
     * @param album
     */
    protected void setAlbum(Album album) {
        this.album = album;
    }

    /**
     * used to get the artist of the album
     * @return artist
     */
    public Artist getPerformer() {
        return performer;
    }

    /**
     * used to set an artist
     * @param performer
     */
    public void setPerformer(Artist performer) {
        this.performer = performer;
    }

    /**
     * used to view an album object
     */
    public String toString() {
        return super.toString() + " " + this.performer + " " + this.album + " " + this.duration;
    }

    /**
     * method for writing into SQL
     */
    public String toSQL() {
        return "insert into songs (id, name, album, artist) values (" + this.entityID + ", " + this.name + ", " + album.entityID + ", "
                + performer.entityID  + ");";
    }

    /**
     * @NotNull
     * @param rs
     * method for retrieving data from SQL
     */
    public void fromSQL(ResultSet rs) {
        try {
            this.entityID = rs.getInt("id");
            this.name = rs.getString("name");
        } catch(SQLException e) {
            System.out.println("SQL Exception" + e.getMessage());
        }

    }



}
