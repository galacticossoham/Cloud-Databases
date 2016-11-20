package app_kvEcs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ECSClient {
	
	static String[] tokens;
	static String[] returntokens;
	static String[][] sData;
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
    	
        String sCurrentLine;
    	System.out.println(new File(".").getAbsolutePath());
    	BufferedReader br = new BufferedReader(new FileReader("example.txt"));
    	StringBuilder builder = new StringBuilder();
    	while ((sCurrentLine = br.readLine()) != null) {
    			builder.append(sCurrentLine);
    			builder.append('\n');
    	}
    			StringBuilder metabuilder = new StringBuilder();
    	 
    	        String result= builder.toString();
    	        String[] rows = result.split("\n");
    	        for (int i = 0; i < rows.length; i++) {
    	        
    	        	String[] columns = rows[i].split(" ");
    	        	sData[i]=columns;
    	        	metabuilder.append(columns[1]);
    	        	metabuilder.append(",");
    	        	metabuilder.append(columns[2]);
    	        	metabuilder.append(",");
    	        	metabuilder.append(hash(columns[1]+columns[2]).toString());
    	        	metabuilder.append("'\n'");
    	        
    	        }
    	        
    	          System.out.println(metabuilder.toString());
    	        
    	        
    	System.out.println("example.txt");
    	    
    	    
    }


    private static BigInteger hash(String node) throws NoSuchAlgorithmException,UnsupportedEncodingException
    {
    	MessageDigest md5 = MessageDigest.getInstance("MD5");
    	byte[] checksum = md5.digest(node.getBytes("UTF-8"));
    	return new BigInteger(1, checksum);
    }


    
    private static void userInput(String userinput){
    	
    	String input = userinput;
    	
    	returntokens = splitCommandAndData(input);
    	processMessage();

    	}
    private static void processMessage(){

		if(returntokens.length > 0){

			//Logging.FILE_LOGGER.debug("Number of tokens greater than 0");
			setCommand(tokens[0]);
			String returnCommand = getCommand().toLowerCase();
			switch(returnCommand){

			case "start":
				
				initService(int numberOfNodes, int cacheSize, String displacementStrategy);
				
				//Logging.FILE_LOGGER.debug("Command : connect");
					
				// if connection is already there or not.
									
						
    private static String[] splitCommandAndData(String input){

		tokens = input.trim().split("\\s+");
		//Logging.FILE_LOGGER.debug("Message trimmed in tokens");
		
		return tokens;
	}
    
    private String getCommand() {
		return command;
	}

	private void setCommand(String command) {
		this.command = command;
	}	
    
	public static String[][] getsData() {
		return sData;
	}

	public static void setsData(String[][] sData) {
		ECSClient.sData = sData;
	}
}
