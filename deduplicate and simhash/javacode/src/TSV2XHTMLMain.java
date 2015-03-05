import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.xml.sax.SAXException;

public class TSV2XHTMLMain {
	static String filename = "";

	public static void main(String[] args) throws IOException, TikaException,
			SAXException, TransformerConfigurationException {
		InputStream doc = new FileInputStream(args[0]);
		TSVParser parser = new TSVParser();
		Metadata metadata = new Metadata();
		StringWriter sw = new StringWriter();
		SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory
				.newInstance();
		TransformerHandler handler = factory.newTransformerHandler();
		handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "html");
		handler.setResult(new StreamResult(sw));
		File file = new File(args[1]);
		ParseContext context = new ParseContext();
		parser.parse(doc, handler, metadata, context);
		String xml = sw.toString();

		file.createNewFile();
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		out.write(xml);
		out.flush();
		out.close();

	}
}
