import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

public class Library {
    private static ArrayList<Song> songs;

    public Library() {
        songs = new ArrayList<Song>();
    }

    /**
     * method for finding if a song is in songs
     */
    public boolean findSong(Song s) {
        return songs.contains(s);
    }

    /**
     * method for retrieving songs
     */
    public ArrayList<Song> getSongs() {
        return songs;
    }

    /**
     * @param s
     * method for adding songs to the arraylist
     */
    public static void addSong(Song s) {
        songs.add(s);
    }

    /**
     * method for converting songs contained in the Library to XML
     */
    public static void toXML(){
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element root = doc.createElement("Library");

            for (Song s : songs) {
                Element song = doc.createElement("Song");

                Element name = doc.createElement("name");
                name.setTextContent(s.getName());
                song.appendChild(name);

                Element artist = doc.createElement("artist");
                artist.setTextContent(s.getPerformer().getName());
                song.appendChild(artist);

                Element album = doc.createElement("album");
                album.setTextContent(s.getAlbum().getName());
                song.appendChild(album);
                root.appendChild(song);
            }

            doc.appendChild(root);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(writer));

            System.out.println(writer.toString());

            FileOutputStream fop = null;
            File file;
            try {

                file = new File("newfile1.xml");
                fop = new FileOutputStream(file);

                if (!file.exists()) {
                    file.createNewFile();
                }

                String xmlString =result.getWriter().toString();
                System.out.println(xmlString);
                byte[] contentInBytes = xmlString.getBytes();

                fop.write(contentInBytes);
                fop.flush();
                fop.close();

                System.out.println("Done");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fop != null) {
                        fop.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (ParserConfigurationException e){
            System.out.println(e);
        }catch (TransformerException e){
            System.out.println(e);
        }
    }


}
