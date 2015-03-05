import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.tika.parser.AbstractParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Set;

import org.apache.tika.config.ServiceLoader;
import org.apache.tika.detect.AutoDetectReader;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.CloseShieldInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.txt.TXTParser;
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class TSVParser extends AbstractParser {
	private static final long serialVersionUID = -6656102320836888910L;

	private static final Set<MediaType> SUPPORTED_TYPES = Collections
			.singleton(MediaType.TEXT_PLAIN);

	private static final ServiceLoader LOADER = new ServiceLoader(
			TXTParser.class.getClassLoader());

	public Set<MediaType> getSupportedTypes(ParseContext context) {
		return SUPPORTED_TYPES;
	}

	public void parse(
			// Locator locator = new Locator();
			InputStream stream, ContentHandler handler, Metadata metadata,
			ParseContext context) throws IOException, SAXException,
			TikaException {
		// Automatically detect the character encoding
		AutoDetectReader reader = new AutoDetectReader(
				new CloseShieldInputStream(stream), metadata, context.get(
						ServiceLoader.class, LOADER));
		try {
			Charset charset = reader.getCharset();
			MediaType type = new MediaType(MediaType.TEXT_PLAIN, charset);
			metadata.set(Metadata.CONTENT_TYPE, type.toString());
			// deprecated, see TIKA-431
			metadata.set(Metadata.CONTENT_ENCODING, charset.name());

			XHTMLContentHandler xhtml = new XHTMLContentHandler(handler,
					metadata);
			xhtml.startDocument();
			xhtml.startElement("table");
			xhtml.startElement("tr");
			// write first row
			xhtml.element("td", "postedDate");
			xhtml.element("td", "location");
			xhtml.element("td", "department");
			xhtml.element("td", "title");
			xhtml.element("td", "empty");
			xhtml.element("td", "salary");
			xhtml.element("td", "start");
			xhtml.element("td", "duration");
			xhtml.element("td", "jobtype");
			xhtml.element("td", "applications");
			xhtml.element("td", "company");
			xhtml.element("td", "contactPerson");
			xhtml.element("td", "phoneNumber:");
			xhtml.element("td", "faxNumber:");
			xhtml.element("td", "place");
			xhtml.element("td", "latitude");
			xhtml.element("td", "longitude");
			xhtml.element("td", "firstSeenDate");
			xhtml.element("td", "url");
			xhtml.element("td", "lastSeenDate");
			xhtml.endElement("tr");
			String s = null;
			while ((s = reader.readLine()) != null) {// read every line of the
														// tsv file
				int n = 0;
				xhtml.startElement("tr");
				String items[] = s.split("	");// split tab so that the value is
												// in the element
				for (String item : items) {
					if (!"".equals(item)) {
						// process the specific character
						item = item.replaceAll("&", "&amp;");
						item = item.replaceAll("<", "&lt;");
						item = item.replaceAll(">", "&gt;");
						item = item.replaceAll("\"", "&quot;");
						item = item.replaceAll("'", "&apos;");
						item = StringEscapeUtils.unescapeHtml4(item)
								.replaceAll("[^\\x20-\\x7e]", "");
						xhtml.element("td", item);
						n++;
					} else {
						xhtml.startElement("td");
						xhtml.endElement("td");
						n++;
					}
				}
				xhtml.endElement("tr");

			}
			xhtml.endElement("table");
			xhtml.endDocument();
		} finally {
			reader.close();

		}

	}

}
