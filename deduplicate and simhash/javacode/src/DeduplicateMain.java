
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.xml.sax.ContentHandler;


public class DeduplicateMain {
	public static String inputfilename="";
	public static HashMap<String, HashMap<SimHash, String>> bigHash = new HashMap<String, HashMap<SimHash, String>>();
	public static int count = 0;
	public static int nonduplicate = 0;
	
	public static void main(String[] args) throws Exception { 
		inputfilename = args[0];
		long startTime =System.currentTimeMillis();
		File document = new File(inputfilename);
		File[] fileListName = document.listFiles();
		int num=0;
		
		for(File file1:fileListName)
		{
			StringBuilder items = new StringBuilder();
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
			    
			    items.append(ch.toString());
			    String[] item = items.toString().split("\n");
			    
				for (String singleItem:item) {
					
					count++;
					JSONParser jsonParser = new JSONParser();
					JSONObject jsonObject = (JSONObject) jsonParser.parse(singleItem);
					String application = (String) jsonObject.get("applications");
					String location = (String) jsonObject.get("location");
					String department = (String) jsonObject.get("department");
					String company = (String) jsonObject.get("company");
					String title = (String) jsonObject.get("title");
					String jobtype = (String) jsonObject.get("jobtype");
					String nearString = application;
					String exactString = location + " " + department + " "
							+ company + " " + title + " " + jobtype;
					//System.out.println(exactString);
					SimHash nearHash = new SimHash(nearString);
					if (bigHash.containsKey(exactString)) {
						HashMap<SimHash, String> smallHash = bigHash
								.get(exactString);
						Iterator iter = smallHash.entrySet().iterator();
						int flag = 0;
						while (iter.hasNext()) {
							Map.Entry<SimHash, String> entry = (Map.Entry<SimHash, String>) iter.next();
							SimHash simhash = entry.getKey();
							if (nearHash.similarity(simhash) > 0.6) {	
								flag = 1;
								break;
							} 
						}
						if (flag == 0) {
							smallHash.put(nearHash, String.valueOf(count));
							nonduplicate++;
						}
					} else {
						HashMap<SimHash, String> smallHash = new HashMap<SimHash, String>();
						smallHash.put(nearHash, String.valueOf(count));
						bigHash.put(exactString, smallHash);
						nonduplicate++;
					}
					//System.out.println(nonduplicate + "/ "+ count);
				}//for item
			    
		    }// if tsv
		  }//end of for tsv file
		System.out.println(nonduplicate + "/ "+ count);
		long endTime = System.currentTimeMillis();
		System.out.print(endTime-startTime);
	}
}
