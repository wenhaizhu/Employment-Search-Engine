
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.xml.sax.ContentHandler;




public class CrawlerMain {
	public static String inputfilename="";
	public static void main(String[] args) throws Exception { 
		inputfilename = args[0];
		long startTime =System.currentTimeMillis();
		File document = new File(inputfilename);
		File[] fileListName = document.listFiles();
		int num=0;
		for(File file1:fileListName)
		{
			String filePre=file1.getName();
		    String prefix=filePre.substring(filePre.lastIndexOf(".")+1);
		    if(prefix.equalsIgnoreCase("tsv"))
		    {
				InputStream doc =new FileInputStream(file1);
				TSVParser parser = new TSVParser();
				Metadata metadata = new Metadata();
				StringWriter sw = new StringWriter();
			    SAXTransformerFactory factory = (SAXTransformerFactory)
			    SAXTransformerFactory.newInstance();
			    TransformerHandler handler = factory.newTransformerHandler();
			    handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "html");
			    handler.setResult(new StreamResult(sw));
				ParseContext context = new ParseContext();
				parser.parse(doc, handler, metadata,context);
				String xml = sw.toString();
				
				
			    InputStream is = new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8")));;
		
			    ContentHandler ch =new JSONTableContentHandler();
			    XHTMLParser xmlparser = new XHTMLParser();
		
			    xmlparser.parse(is, ch, metadata, new ParseContext());
			    is.close();
			    String[] items = ch.toString().split("\n");
			    
			    System.out.println(ch.toString());
			    int j =0;
			    for(j=0;j<items.length;j++)
			    {
			    	items[j] = items[j].replaceAll("\\\\", "\\\\\\\\");
			    	File fileName = new File(args[1]+num+"_"+j+".json");
			    	fileName.createNewFile();
			    	BufferedWriter out2 = new BufferedWriter(new FileWriter(fileName));
			    	out2.write(items[j]);
			    	out2.flush();
			    	out2.close();
			    }
			    num++;

		    }
		  }
		long endTime = System.currentTimeMillis();
		System.out.print("time: " + (endTime-startTime));
	}
}
