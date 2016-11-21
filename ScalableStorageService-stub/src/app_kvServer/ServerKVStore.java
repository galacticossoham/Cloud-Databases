package app_kvServer;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ServerKVStore {
		
	public static boolean PUT_STATUS;
	private static Node KVPair, rootelement = null; 
	private static NodeList KVPairs, KVPairChildren = null;
	private static Document doc = null;
	private static DocumentBuilder docBuilder = null;
	private static String getresult = null;
	private static String STATUS;
	
	static String get(String key){
	    	try {
				parseDatabase();
			} catch (SAXException | IOException | ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setStatus("GET_ERROR");
				
			}
	    	if(rootelement==null){
	    		getresult = "Key does not exist!!";
	    	}
	    	else{
	    		boolean novalue = true;
	    		KVPairs = rootelement.getChildNodes();
				for(int i = 0; i < KVPairs.getLength(); i++)
				{
					
					KVPair = KVPairs.item(i);
					KVPairChildren = KVPair.getChildNodes();
					for (int y = 0; y < KVPairChildren.getLength(); y++) 
					{
						if(key.equals(KVPairChildren.item(y).getTextContent()))
						{
							// found existing key
							novalue = false;
							setStatus("GET_SUCCESS");
							getresult = "KEY: " + key + " VALUE: "+ KVPairChildren.item(y).getNextSibling().getTextContent();		
						}
						
					}
				}
				
				if(novalue){
					getresult = "The key you are looking for does not exist, please try again!";
					setStatus("GET_ERROR");
				}
	
	    	}
	    	return getresult;
	    	
	    }
	
		static void put(String key, String value){
		   
		    try {
				parseDatabase();
			} catch (SAXException | IOException | ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setStatus("PUT_ERROR");
			}
		   	
			if(rootelement==null){
				  // DB does not exist; creating root element
				  doc = docBuilder.newDocument();
				  rootelement = doc.createElement("KVStore");
				  doc.appendChild(rootelement);
				  makeNewEntry(key, value);
				  setStatus("PUT_SUCCESS");
			  }
			  
			  else /* DB already exists*/ {
				  	boolean makenewentry = true;
					KVPairs = rootelement.getChildNodes();
					
					for(int i = 0; i < KVPairs.getLength(); i++)
					{
						
						KVPair = KVPairs.item(i);
						KVPairChildren = KVPair.getChildNodes();
						for (int y = 0; y < KVPairChildren.getLength(); y++) 
						{
							if(key.equals(KVPairChildren.item(y).getTextContent()))
							{
								makenewentry = false;
								// modify existing key value
								KVPairChildren.item(y).getNextSibling().setTextContent(value);
								setStatus("PUT_UPDATE");
							}
							
						}
					}
						
							if(makenewentry){
								makeNewEntry(key, value);
								setStatus("PUT_SUCCESS");
							}
			  }
			
			 
			  try {
				writeToPersistentStorage();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setStatus("PUT_ERROR");
			}
		
	}
		static void put(String key){
			
			boolean foundkey = false;
			try {
				parseDatabase();
			} catch (SAXException | IOException | ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setStatus("DELETE_ERROR");
			}
			KVPairs = rootelement.getChildNodes();
			
			for(int i = 0; i < KVPairs.getLength(); i++)
			{
				
				KVPair = KVPairs.item(i);
				KVPairChildren = KVPair.getChildNodes();
				for (int y = 0; y < KVPairChildren.getLength(); y++) 
				{
					if(key.equals(KVPairChildren.item(y).getTextContent()))
					{
						foundkey = true;
					}
				}
				if(foundkey)
				{
					System.out.println("foundkey");
					rootelement.removeChild(KVPair);
					setStatus("DELETE_SUCCESS");
					foundkey = false;
				}
			}
			try {
				writeToPersistentStorage();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setStatus("DELETE_ERROR");
			}
		}

		private static void parseDatabase() throws SAXException, IOException, ParserConfigurationException {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.parse("KVStore\\Store.xml");
			rootelement = doc.getFirstChild();
			
		}

		public static void writeToPersistentStorage() throws TransformerException {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("KVStore\\Store.xml"));

			  // Output to console for testing
		 // result = new StreamResult(System.out);

		  transformer.transform(source, result);

		  System.out.println("Server Persistent Storage Updated!");
		 

			
		}

		private static void makeNewEntry(String key, String value) {
			// create KVPair
			  Element KVPair = doc.createElement("KVPair");
			  rootelement.appendChild(KVPair);

			  // Create and insert Key
			  Element KVkey = doc.createElement("Key");
			  KVkey.appendChild(doc.createTextNode(key));
			  KVPair.appendChild(KVkey);

			  // Create and insert Value
			  Element KVvalue = doc.createElement("Value");
			  KVvalue.appendChild(doc.createTextNode(value));
			  KVPair.appendChild(KVvalue);
			  System.out.println("created entry");

		}
		public static String getStatus() {
			return STATUS;
		}
		public static void setStatus(String sTATUS) {
			STATUS = sTATUS;
		}

		
}