import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LibraryTest {

    Library Library;
    Library Library2;

    @BeforeEach
    void setUp(){
        Library = new Library();
        Library2 = new Library();

        Song s1 = new Song();
        Song s2 = new Song();
        Song s3 = new Song();
        Song s4 = new Song();
        Song s5 = new Song();
        s1.setName("Dear Prudence");
        s1.setPerformer(new Artist("Beatles"));
        s1.setAlbum(new Album("The White Album"));

        s2.setName("Glass Onion");
        s2.setPerformer(new Artist("Beatles"));
        s2.setAlbum(new Album("The White Album"));

        s3.setName("Lucy In The Sky With Diamonds");
        s3.setPerformer(new Artist("Beatles"));
        s3.setAlbum(new Album("Sgt. Pepper's Lonely Hearts Club Band"));

        s4.setName("Lovely Rita");
        s4.setPerformer(new Artist("Beatles"));
        s4.setAlbum(new Album("Sgt. Pepper's Lonely Hearts Club Band"));

        s5.setName("Lovely Rita");
        s5.setPerformer(new Artist("Beatles"));
        s5.setAlbum(new Album("Sgt. Pepper's Lonely Hearts Club Band"));

        Library.addSong(s1);
        Library.addSong(s2);
        Library.addSong(s3);
        Library.addSong(s4);
    }

    @Test
    void toXML(){
        Library.toXML();
    }

}