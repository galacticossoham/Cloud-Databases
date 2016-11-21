package app_kvEcs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import app_kvServer.Manager;

public class ECSClient {
	
	static String[] tokens;
	static String[] returntokens;
	static String[][] sData;
	static String metadata;
	private static int numberofnodes = 0;
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
    	        	metabuilder.append(Manager.hash(columns[1]+columns[2]).toString());
    	        	metabuilder.append("'\n'");
    	        
    	        }
    	        
    	          metadata= metabuilder.toString();
    	            	    
    	    
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
				initService(2, 10, "fifo");
				sendData("", returnCommand);
				break;
				
			case "stop":
				sendData("", returnCommand);
				
				break;
			
			case "shutdown":
				sendData("", returnCommand);
			break;
			case "add":
			break;
			case "remove":
				break;
			
		}
				
				//Logging.FILE_LOGGER.debug("Command : connect");
					
				// if connection is already there or not.
									
						
    private static String[] splitCommandAndData(String input){

		tokens = input.trim().split("\\s+");
		//Logging.FILE_LOGGER.debug("Message trimmed in tokens");
		
		return tokens;
	}
    
    private void start() {
    	initService(2, 10, "fifo");
    	
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
	private static boolean initService(int numberOfNodes, int cacheSize, String displacementStrategy) {
		
		numberofnodes = numberOfNodes;
		String[][] temp = getsData();
		
		for(int i=0; i<numberOfNodes; i++)
		{
			SSHPublicKeyAuthentication.sshConnection("localhost", Integer.parseInt(temp[i][2]), "FIFO", 10);
		}
		sendData(metadata, "meta");
		
		return true;
	}
	private static void sendData(String data, String command){
		
		String[][] temp = getsData();

		for(int i=0; i < numberofnodes;i++)
		{
			try {
				KVStore.connect(temp[i][1], Integer.parseInt(temp[i][2]));
				KVStore.put(command, data);
				KVStore.disconnect();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
		
		
}
