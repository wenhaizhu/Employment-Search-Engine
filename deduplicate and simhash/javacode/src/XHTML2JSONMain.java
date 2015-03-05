
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.transform.TransformerConfigurationException;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
public class XHTML2JSONMain {
    public static String filename="";
	public static void main(String[] args) throws IOException, TikaException, SAXException, TransformerConfigurationException { 
		filename = args[0];
		File file = new File(args[0]);
	    InputStream is = new FileInputStream(file);
	    Metadata metadata = new Metadata();
	    ContentHandler ch =new JSONTableContentHandler();
	    XHTMLParser parser = new XHTMLParser();

	    String mimeType = new Tika().detect(file);
	    metadata.set(Metadata.CONTENT_TYPE, mimeType);

	    parser.parse(is, ch, metadata, new ParseContext());
	    is.close();
	    String [] items = ch.toString().split("\n");
	    int i =0;
	    for(i=0;i<items.length;i++)
	    {
	    	File fileName = new File(args[1]+i+".json");
	    	fileName.createNewFile();
	    	BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
	    	out.write(items[i]);
	    	out.flush();
	    	out.close();
	    }
	    System.out.println(ch.toString());
	  }
	}
