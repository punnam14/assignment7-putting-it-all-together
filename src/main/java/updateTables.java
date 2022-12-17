import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class updateTables {
    static String artistName;
    static updateTables app = new updateTables();
    static RetriveData getData = new RetriveData();

    /**
     * @return artistName
     * Takes a string user input for artist name and returns the string
     */
    public static String artistReturn() {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter an Artist to see their Albums: ");
        artistName = myObj.nextLine();
        return artistName;
    }

    /**
     * @return artistName
     * method for returning the artist name
     */
    public static String artistReturnNoInput() {
        return artistName;
    }

    /**
     * @return URL
     * method for creating a URL that access the AudiDB link for artist data
     */
    public static URL createURLArtist() throws MalformedURLException {
        Scanner myObj = new Scanner(System.in);
        String requestURL = "https://www.theaudiodb.com/api/v1/json/523532/search.php?s=";
        System.out.println("Enter an Artist you want to add: ");
        String artistNameData = myObj.nextLine();
        URL u;
        u = new URL(requestURL + artistNameData);
        return u;
    }

    /**
     * @return URL
     * method for creating a URL that access the AudiDB link for individual album data
     */
    public static URL createURLAlbumCount() throws MalformedURLException {
        String requestURL = "https://www.theaudiodb.com/api/v1/json/523532/discography.php?s=";
        String artist = artistReturnNoInput();
        URL u;
        u = new URL(requestURL + artist);
        return u;
    }

    /**
     * @return URL
     * method for creating a URL that access the AudiDB link for individual album data
     * calls the artistReturn method for taking in user input for artist name
     */
    public static URL createURLAlbums() throws MalformedURLException {
        String requestURL = "https://theaudiodb.com/api/v1/json/523532/searchalbum.php?s=";
        String artist = artistReturn();
        URL u;
        u = new URL(requestURL + artist);
        return u;
    }

    /**
     * @param albumID
     * @return URL
     * method for creating a URL that access the AudiDB link for individual track data by taking in albumID as an input
     */
    public static URL createURLTrack(String albumID) throws MalformedURLException {
        String requestURL = "https://theaudiodb.com/api/v1/json/2/track.php?m=";
        URL u;
        u = new URL(requestURL + albumID);
        return u;
    }

    /**
     * @param u
     * @return response
     * method that takes in any URL and establishes a connection to AudioDB and returns a string builder called response
     */
    public static StringBuilder createConnection(URL u) throws IOException {
        StringBuilder response = new StringBuilder();

        URLConnection connection = u.openConnection();
        HttpURLConnection httpConnection = (HttpURLConnection) connection;
        InputStream instream = connection.getInputStream();
        Scanner in = new Scanner(instream);
        while (in.hasNextLine()) {
            response.append(in.nextLine());
        }
        return response;
    }

    /**
     * @param response
     * @return JSONobject
     * method that takes an input of string string builder response, parses it and returns a JSONObject
     */
    public static JSONObject JSONParser(StringBuilder response) throws ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(response.toString());
        JSONObject jsonObject = (JSONObject) obj;
        return jsonObject;
    }

    /**
     * @param jsonObject
     * method that inserts album data into the sql database and inserts the album artist into the artist table
     */
    public static void insertAlbums(JSONObject jsonObject) throws IOException, ParseException {
        Scanner myObj = new Scanner(System.in);

        ArrayList<String> indAlbumNames = new ArrayList<String>();
        String albumName;
        int albumNumber;

        JSONArray albums = (JSONArray) jsonObject.get("album"); // get the list of all albums returned.
        for (int i = 0; i < albums.size(); i++) {
            JSONObject albumNames = (JSONObject) albums.get(i);
            String indAlbum = (String) albumNames.get("strAlbum");
            System.out.println(+i + " " + indAlbum + "\n");
            indAlbumNames.add(indAlbum);
        }
        System.out.println("Enter the number of the Album you want to add: ");
        albumNumber = myObj.nextInt();

        JSONObject idAlb = (JSONObject) albums.get(albumNumber);
        String idAlbum = (String) idAlb.get("idAlbum");
        JSONObject idArt = (JSONObject) albums.get(albumNumber);
        String idArtist = (String) idArt.get("idArtist");
        JSONObject strArt = (JSONObject) albums.get(albumNumber);
        String strArtist = (String) strArt.get("strArtist");
        String albumCount = fetchAlbumCount();
        String trackCount = fetchTrackCount(idAlbum);

        albumName = indAlbumNames.get(albumNumber);
        app.insertAlbum(idAlbum, albumName, idArtist, strArtist, albumCount, trackCount);
        app.insertArtist(idArtist, strArtist, "null", "null");
    }

    /**
     * @param jsonObject
     * method that inserts specified artist data into the artist table
     */
    public static void insertArtists(JSONObject jsonObject) {
        JSONArray artists = (JSONArray) jsonObject.get("artists");
        JSONObject singleArtist = (JSONObject) artists.get(0);

        String idArtist = (String) singleArtist.get("idArtist");
        String strArtist = (String) singleArtist.get("strArtist");
        String strGenre = (String) singleArtist.get("strGenre");
        String strMood = (String) singleArtist.get("strMood");

        app.insertArtist(idArtist, strArtist, strGenre, strMood);
    }

    /**
     * method that inserts song data into the song table
     * it first asks the user to enter an artist of choice and displays albums by that artist
     * it then displays a song list for the album of choice and adds it to the song table
     */
    public static void insertSongs() throws IOException, ParseException {
        URL u = createURLAlbums();
        StringBuilder response = createConnection(u);
        JSONObject jsonObject = JSONParser(response);
        ArrayList<String> indTrackNames = new ArrayList<String>();

        Scanner myObj = new Scanner(System.in);
        ArrayList<String> indAlbumNames = new ArrayList<String>();
        int trackNumber;
        int albumNumber;

        JSONArray albums = (JSONArray) jsonObject.get("album");
        for (int i = 0; i < albums.size(); i++) {
            JSONObject albumNames = (JSONObject) albums.get(i);
            String indAlbum = (String) albumNames.get("strAlbum");
            System.out.println(+i + " " + indAlbum + "\n");
            indAlbumNames.add(indAlbum);
        }
        System.out.println("Enter the Album for displaying Songs: ");
        albumNumber = myObj.nextInt();

        JSONObject idAlb = (JSONObject) albums.get(albumNumber);
        String idAlbum = (String) idAlb.get("idAlbum");

        URL uTrack = createURLTrack(idAlbum);
        StringBuilder responseTrack = createConnection(uTrack);
        JSONObject jsonObjectTrack = JSONParser(responseTrack);
        JSONArray tracks = (JSONArray) jsonObjectTrack.get("track");

        for (int i = 0; i < tracks.size(); i++) {
            JSONObject trackNames = (JSONObject) tracks.get(i);
            String indtrack = (String) trackNames.get("strTrack");
            System.out.println(+i + " " + indtrack + "\n");
            indTrackNames.add(indtrack);
        }
        Scanner myObj2 = new Scanner(System.in);
        System.out.println("Enter the Song number you want to add: ");
        int trackNum = myObj2.nextInt();

        JSONObject singleSong = (JSONObject) tracks.get(trackNum);
        String trackIDTrack = (String) singleSong.get("idTrack");
        String artistIDTrack = (String) singleSong.get("idArtist");
        String artistNameTrack = (String) singleSong.get("strArtist");
        String trackNameTrack = (String) singleSong.get("strTrack");
        String genreTrack = (String) singleSong.get("strGenre");
        String albumNameTrack = (String) singleSong.get("strAlbum");
        String albumIDTrack = (String) singleSong.get("idAlbum");

        app.insertSong(trackIDTrack, artistIDTrack, artistNameTrack, trackNameTrack, genreTrack, albumNameTrack, albumIDTrack);

    }

    /**
     * creates a connection to the database for inserting data
     */
    private Connection connect() {
        String url = "jdbc:sqlite:music.db";
        //String url = "jdbc:sqlite:C:/Users/1998p/Desktop/sqlite-tools/music.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * creates a connection to the database and inserts album data into the albums table
     */
    public void insertAlbum(String ID, String name, String artistID, String artistName, String albumCount, String trackCount) {
        String sql = "INSERT INTO albums(albumID,albumName,artistID, artistName, albumCount, trackCount) VALUES(?,?,?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ID);
            pstmt.setString(2, name);
            pstmt.setString(3, artistID);
            pstmt.setString(4, artistName);
            pstmt.setString(5, albumCount);
            pstmt.setString(6, trackCount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * creates a connection to the database and inserts artist data into the artists table
     */
    public void insertArtist(String artistID, String artistName, String artistGenre, String artistMood) {
        String sql = "INSERT INTO artists(artistID, artistName, artistGenre, artistMood) VALUES(?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, artistID);
            pstmt.setString(2, artistName);
            pstmt.setString(3, artistGenre);
            pstmt.setString(4, artistMood);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * creates a connection to the database and inserts song data into the songs table
     */
    public void insertSong(String trackID, String artistID, String artistName, String trackName, String genreTrack, String albumName, String albumID) {
        String sql = "INSERT INTO songs(trackID, artistID, artistName, trackName, genreTrack, albumName, albumID) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, trackID);
            pstmt.setString(2, artistID);
            pstmt.setString(3, artistName);
            pstmt.setString(4, trackName);
            pstmt.setString(5, genreTrack);
            pstmt.setString(6, albumName);
            pstmt.setString(7, albumID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param idAlbum
     * @return finalCount
     * takes in an input for ID of an album and then connects to Audio DB to fetch tracks in the album
     * Counts the number of tracks in the album and returns the number of songs
     */
    public static String fetchTrackCount(String idAlbum) throws IOException, ParseException {
        String requestURL = "https://theaudiodb.com/api/v1/json/523532/track.php?m=";
        URL uTrack;
        uTrack = new URL(requestURL + idAlbum);
        StringBuilder responseTrack = createConnection(uTrack);
        JSONObject jsonObjectTrack = JSONParser(responseTrack);
        JSONArray tracks = (JSONArray) jsonObjectTrack.get("track");
        int trackCount = 0;
        String singleStringTrack = responseTrack.toString();
        singleStringTrack = singleStringTrack.replaceAll("[^A-Za-z]+", " ");
        String[] words = singleStringTrack.split(" ");
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals("idTrack")) {
                trackCount++;
            }
        }
        String finalCount = String.valueOf(trackCount);
        return finalCount;
    }

    /**
     * @return finalCount
     * connects to Audio DB to fetch albums for a specified artist
     * counts the number of albums and returns the total number of albums for an artist
     */
    public static String fetchAlbumCount() throws IOException {
        URL u = createURLAlbumCount();
        StringBuilder response = createConnection(u);
        int albumCount = 0;
        String singleString = response.toString();
        singleString = singleString.replaceAll("[^A-Za-z]+", " ");
        String[] words = singleString.split(" ");
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals("strAlbum")) {
                albumCount++;
            }
        }
        String finalCount = String.valueOf(albumCount);
        return finalCount;
    }

    public static void main(String[] args) throws IOException, ParseException {
        int option;
        do {
            System.out.println("------------------------------" + "\n" +
                    "Welcome to your Music Player" + "\n" +
                    "------------------------------");
            System.out.println("Choose an option by entering a number: "
                    + "\n" + "1) Add an Album"
                    + "\n" + "2) Add a Song"
                    + "\n" + "3) Add an Artist"
                    + "\n" + "4) Display my Albums"
                    + "\n" + "5) Display my Songs"
                    + "\n" + "6) Display my Artists"
                    + "\n" + "7) Generate Top 5 Pop-Rock Playlist"
                    + "\n" + "8) Generate Top 5 Pop-Rock Playlist XML"
                    + "\n" + "9) Exit Music Player");
            System.out.println("Enter number option: ");
            option = -1;
            Scanner sc = new Scanner(System.in);
            if (sc.hasNextInt()) {
                option = sc.nextInt();
                switch (option) {
                    case 1:
                        URL u = createURLAlbums();
                        StringBuilder response = createConnection(u);
                        JSONObject jsonObject = JSONParser(response);
                        insertAlbums(jsonObject);
                        System.out.println("Successfully added the Album!");
                        break;
                    case 2:
                        insertSongs();
                        System.out.println("Successfully added the Song!");
                        break;
                    case 3:
                        URL u1 = createURLArtist();
                        StringBuilder response1 = createConnection(u1);
                        JSONObject jsonObject1 = JSONParser(response1);
                        insertArtists(jsonObject1);
                        System.out.println("Successfully added the artist!");
                        break;
                    case 4:
                        System.out.println("Showing you your Albums: ");
                        getData.fromSQLAlbums();
                        break;
                    case 5:
                        System.out.println("Showing you your Songs: ");
                        getData.fromSQLSongs();
                        break;
                    case 6:
                        System.out.println("Showing you your Artists: ");
                        getData.fromSQLArtists();
                        break;
                    case 7:
                        System.out.println("Showing you your Top 5 Pop-Rock Playlist: ");
                        getData.fromSQLSongsPlaylist();
                        break;
                    case 8:
                        System.out.println("Showing you your Top 5 Pop-Rock Playlist XML Version: ");
                        System.out.println("\n");
                        getData.fromSQLSongsPlaylistXML();
                        break;
                    case 9:
                        System.out.println("Thank you for using Music Player");
                }
            }
        } while (option != 9);
    }
}
