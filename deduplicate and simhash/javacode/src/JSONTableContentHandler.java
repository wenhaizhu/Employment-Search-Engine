
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.tika.sax.ToTextContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class JSONTableContentHandler extends ToTextContentHandler {
	
	private String[] tags ={"postedDate","location","department","title","empty","salary","start",
			"duration","jobtype","applications","company","contactPerson","phoneNumber",
			"faxNumber","place","latitude","longitude","firstSeenDate","url","lastSeenDate"};


   
    ContentHandler handler;
    public JSONTableContentHandler(OutputStream stream)
            throws UnsupportedEncodingException {
        super(stream);
    }

    public JSONTableContentHandler() {
        super();
    }
 

    
  
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
    	if(ch.length==1&&ch[0]=='\n')
    		writeEscaped(ch,0,0);
    	else
    		writeEscaped(ch,start,start+length);
    		
    }

    

    
    protected void write(char ch) throws SAXException {
        super.characters(new char[] { ch }, 0, 1);
    }

    
    protected void write(String string) throws SAXException {
        super.characters(string.toCharArray(), 0, string.length());
    }
    
    
    //make it as a new function
    protected void write(char[] ch, int start, int end) throws SAXException {
        super.characters(ch, start, end-start);
    }

    private void writeEscaped(char[] ch, int from, int to)
            throws SAXException {
    	if((to-from)>0)
    	{
    		int index =0;
    		int start=from;
    		for(int i=from;i<to;i++)
    		{
    			if(ch[i]=='<')
    			{
    				if(i<to-2&&ch[i+1]=='t'&&ch[i+2]=='r')
    				{
    					write("{");
    					continue;
    				}
    				if(i<to-2&&ch[i+1]=='/'&&ch[i+2]=='t'&&ch[i+3]=='r')
    				{
    					write("}");
    					write('\n');
    					continue;
    				}
    				if(i<to-2&&ch[i+1]=='t'&&ch[i+2]=='d')
    				{
    					if(i<to-3&&index<tags.length&&ch[i+3]=='/')
    					{
    						write("\"");
        					write(tags[index++]);
        					write("\"");
        					write(":");
        					write("\"");
        					write("");
        					write("\"");
        					write(",");
        					continue;
    					}
    					else
    					{
    						start=i+4;
    						i=start;
    					}
    					if(index==tags.length)
    						index=0;
    				}
    				if(i<to-3&&ch[i+1]=='/'&&ch[i+2]=='t'&&ch[i+3]=='d')
    				{
    					
    					write("\"");
    					write(tags[index++]);
    					write("\"");
    					write(":");
    					write("\"");
    					write(ch,start,i);
    					write("\"");
    					if(index!=tags.length)
    						write(",");
    					else
    						continue;
    					
    					
    				}
    			}	
    			
    		}
    	}
    }

}
