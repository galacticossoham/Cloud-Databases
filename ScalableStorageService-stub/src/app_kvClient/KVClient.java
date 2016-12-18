package app_kvClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import logger.LogSetup;
import common.messages.KVMessage;
import common.messages.KVMessage.StatusType;
import client.*;


public class KVClient {

	//TODO add Logger and uncomment below statement.
	//private static Logger logger = Logger.getRootLogger();
	
	private static final String PROMPT = "EchoClient> ";
	private BufferedReader stdin;
	private boolean stop = false;
	private KVStore clientKVStore;
	private String serverAddress;
	private int serverPort;
	private String configFile;

	private void updateClientMetadataConfig(String METADATA){
		this.configFile = METADATA;
	}
	private String getCorrectAddress(String key){
		
		// TODO after searching for the responsible server in the metadata file, return its address.
		return "ServerAddress";
	}
	private int getCorrectPort(String serverAddress){
		// TODO get the port mapped to serverAddress
		return 8080;
	}
	private void handleCommand(String cmdLine) {
		String[] tokens = cmdLine.split("\\s+");

		if(tokens[0].equals("quit")) {	
			stop = true;
			clientKVStore.disconnect();
			System.out.println(PROMPT + "Application exit!");
		
		} else if (tokens[0].equals("connect")){
			if(tokens.length == 3) {
				try{
					serverAddress = tokens[1];
					serverPort = Integer.parseInt(tokens[2]);
					clientKVStore = new KVStore(serverAddress, serverPort);
					clientKVStore.connect();
				} catch(NumberFormatException nfe) {
					printError("No valid address. Port must be a number!");
					//logger.info("Unable to parse argument <port>", nfe);
				} catch (UnknownHostException e) {
					printError("Unknown Host!");
					//logger.info("Unknown Host!", e);
				} catch (IOException e) {
					printError("Could not establish connection!");
					//logger.warn("Could not establish connection!", e);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				printError("Invalid number of parameters!");
			}
			
		} else if(tokens[0].equals("disconnect")) {
			//TODO if client is connected, disconnect.
			
		} else if(tokens[0].equals("logLevel")) {
			if(tokens.length == 2) {
				String level = setLevel(tokens[1]);
				if(level.equals(LogSetup.UNKNOWN_LEVEL)) {
					printError("No valid log level!");
					printPossibleLogLevels();
				} else {
					System.out.println(PROMPT + 
							"Log level changed to level " + level);
				}
			} else {
				printError("Invalid number of parameters!");
			}
			
		} else if(tokens[0].equals("help")) {
			printHelp();
		} 
		else if(tokens[0].equals("PUT")){
			// assuming that the input syntax is : PUT KEY VALUE
			// TODO add check whether the client is connected or not.
			String key = tokens[1];
			String value = tokens[2];
			KVMessage kvMessage = null;
			try {
				kvMessage = clientKVStore.put(key, value);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StatusType statusType = kvMessage.getStatus();

			if(statusType.equals(StatusType.PUT)){
				// TODO look what this means
			}
			else if(statusType.equals(StatusType.PUT_ERROR)){
				// assuming that this error might be because the server is not responsible for 
				// this key value.
				
				
				// for testing purposes, getMetaData() will return a String, which will later be converted to a file.
				String METADATA = clientKVStore.getMetadata();
				updateClientMetadataConfig(METADATA);
				serverAddress = getCorrectAddress(key);
				serverPort = getCorrectPort(serverAddress);
				clientKVStore = new KVStore(serverAddress, serverPort);
				try {
					clientKVStore.connect();
					clientKVStore.put(key, value);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			else if(statusType.equals(StatusType.PUT_SUCCESS)){
				
			}
			else if(statusType.equals(StatusType.PUT_UPDATE)){
				
			}
			
			
			
		}
		else if(tokens[0].equals("GET")){
			// assuming GET syntax is GET KEY
			String key = tokens[1];
			
			// TODO check if client is already connected
			KVMessage kvMessage = null;
			try {
				kvMessage = clientKVStore.get(key);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StatusType statusType = kvMessage.getStatus();
			switch(statusType){
			case GET_ERROR:
				String METADATA = clientKVStore.getMetadata();
				updateClientMetadataConfig(METADATA);
				serverAddress = getCorrectAddress(key);
				serverPort = getCorrectPort(serverAddress);
				clientKVStore = new KVStore(serverAddress, serverPort);
				try {
					clientKVStore.connect();
					clientKVStore.get(key);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
			case GET_SUCCESS:
				break;
			default:
				break;
			}
			
		}
		else {
			printError("Unknown command");
			printHelp();
		}
	}
	
	private void printHelp() {
		StringBuilder sb = new StringBuilder();
		sb.append(PROMPT).append("ECHO CLIENT HELP (Usage):\n");
		sb.append(PROMPT);
		sb.append("::::::::::::::::::::::::::::::::");
		sb.append("::::::::::::::::::::::::::::::::\n");
		sb.append(PROMPT).append("connect <host> <port>");
		sb.append("\t establishes a connection to a server\n");
		sb.append(PROMPT).append("send <text message>");
		sb.append("\t\t sends a text message to the server \n");
		sb.append(PROMPT).append("disconnect");
		sb.append("\t\t\t disconnects from the server \n");
		
		sb.append(PROMPT).append("logLevel");
		sb.append("\t\t\t changes the logLevel \n");
		sb.append(PROMPT).append("\t\t\t\t ");
		sb.append("ALL | DEBUG | INFO | WARN | ERROR | FATAL | OFF \n");
		
		sb.append(PROMPT).append("quit ");
		sb.append("\t\t\t exits the program");
		System.out.println(sb.toString());
	}
	
	private void printPossibleLogLevels() {
		System.out.println(PROMPT 
				+ "Possible log levels are:");
		System.out.println(PROMPT 
				+ "ALL | DEBUG | INFO | WARN | ERROR | FATAL | OFF");
	}
	
	public void run() {
		while(!stop) {
			stdin = new BufferedReader(new InputStreamReader(System.in));
			System.out.print(PROMPT);
			
			try {
				String cmdLine = stdin.readLine();
				this.handleCommand(cmdLine);
			} catch (IOException e) {
				stop = true;
				//printError("CLI does not respond - Application terminated ");
			}
		}
	}
	private void printError(String error){
		System.out.println(PROMPT + "Error! " +  error);
	}
private String setLevel(String levelString) {
		
		/*if(levelString.equals(Level.ALL.toString())) {
			logger.setLevel(Level.ALL);
			return Level.ALL.toString();
		} else if(levelString.equals(Level.DEBUG.toString())) {
			logger.setLevel(Level.DEBUG);
			return Level.DEBUG.toString();
		} else if(levelString.equals(Level.INFO.toString())) {
			logger.setLevel(Level.INFO);
			return Level.INFO.toString();
		} else if(levelString.equals(Level.WARN.toString())) {
			logger.setLevel(Level.WARN);
			return Level.WARN.toString();
		} else if(levelString.equals(Level.ERROR.toString())) {
			logger.setLevel(Level.ERROR);
			return Level.ERROR.toString();
		} else if(levelString.equals(Level.FATAL.toString())) {
			logger.setLevel(Level.FATAL);
			return Level.FATAL.toString();
		} else if(levelString.equals(Level.OFF.toString())) {
			logger.setLevel(Level.OFF);
			return Level.OFF.toString();
		} else {
			return LogSetup.UNKNOWN_LEVEL;
		}*/
	
		return null;
	}
	public static void main(String[] args) {
    	KVClient clientApp = new KVClient();
		clientApp.run();
    }

	
}
