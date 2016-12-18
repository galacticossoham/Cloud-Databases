package utils;

import java.util.Iterator;
import java.util.List;

public class MessageOps{
	
	private static final String delimiter = "/";
	
	public static byte[] getRawBytes(String key, String value){
		String concatMsg;
		if(!value.equals(null)){
			concatMsg = key + delimiter + value;
		}
		else{
			concatMsg = key;
		}
		
		return concatMsg.getBytes();
	}
	
	public static String convertBytesToString(List<Integer> receivedMessageInBytes){
		String convertedMessage = new String();
		StringBuilder strbul  = new StringBuilder();
	    Iterator<Integer> iter = receivedMessageInBytes.iterator();
	     while(iter.hasNext())
	     {
	        strbul.append((char)((int)iter.next()));
	        
	     }
	    convertedMessage = strbul.toString();
		return convertedMessage;
	}
}
