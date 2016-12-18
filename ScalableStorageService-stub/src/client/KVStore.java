package client;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


import utils.MessageOps;
import common.messages.KVMessage;

public class KVStore implements KVCommInterface {

	
	/**
	 * Initialize KVStore with address and port of KVServer
	 * @param address the address of the KVServer
	 * @param port the port of the KVServer
	 */
	
	private String address;
	private int port;
	private Socket clientSocket;
	private BufferedOutputStream bufferedOutputStream;
	private BufferedInputStream bufferedInputStream;
	private OutputStream outputStream;
	private InputStream inputStream;
	
	public KVStore(String address, int port) {
		this.address = address;
		this.port = port;
	}
	
	@Override
	public void connect() throws Exception {
		// TODO Auto-generated method stub
		clientSocket = new Socket(address, port);	
		if(clientSocket.isConnected()){
			outputStream = clientSocket.getOutputStream();
			inputStream = clientSocket.getInputStream();
			bufferedOutputStream = new BufferedOutputStream(outputStream);
			bufferedInputStream = new BufferedInputStream(inputStream);
		}
		else{
			System.out.println("Could not connect to the server");
		}

	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		if(clientSocket != null){
			try {
				clientSocket.close();
				outputStream.close();
				inputStream.close();
				bufferedInputStream.close();
				bufferedOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			// TODO uncomment when proper error checking will be implemented.
			/*if(clientSocket.isClosed()){
				connectionMade = false;
				Logging.FILE_LOGGER.debug("Socket is closed");
			}*/
		}
		
	}

	@Override
	public KVMessage put(String key, String value) throws Exception {
		// TODO Auto-generated method stub
		List<Integer> receivedMessageInBytes = new ArrayList<>();
		String receivedMessageString;
		byte[] rawMessageBytes = MessageOps.getRawBytes(key, value);
		bufferedOutputStream.write(rawMessageBytes);
		bufferedOutputStream.write(13);
		bufferedOutputStream.flush();
		
		//TODO: add sleep
		if(bufferedInputStream.available() != 0){
			int c; 
			while((c = bufferedInputStream.read()) != 13){
					
				receivedMessageInBytes.add(c);
			}
			
			receivedMessageString = MessageOps.convertBytesToString(receivedMessageInBytes);
		}
		KVMessage kvMessage;
		// TODO convert receivedMessageString to type KVMessage and return it.
		return null;
	}

	@Override
	public KVMessage get(String key) throws Exception {
		// TODO Auto-generated method stub
		List<Integer> receivedMessageInBytes = new ArrayList<>();
		byte[] rawKey = MessageOps.getRawBytes(key, null);
		String receivedMessageString;
		bufferedOutputStream.write(rawKey);
		bufferedOutputStream.write(13);
		bufferedOutputStream.flush();
		
		// TODO add sleep
		if(bufferedInputStream.available() != 0){
			int c; 
			while((c = bufferedInputStream.read()) != 13){
					
				receivedMessageInBytes.add(c);
			}
			
			receivedMessageString = MessageOps.convertBytesToString(receivedMessageInBytes);
		}
		KVMessage kvMessage;
		
		// TODO convert receivedMessageString to KVMessage and return.
		return null;
	}
	
	public String getMetadata(){
		byte[] rawKey = MessageOps.getRawBytes("GET_METADATA", null);
		String receivedMessageString;
		List<Integer> receivedMessageInBytes = new ArrayList<>();
		
		try {
			bufferedOutputStream.write(rawKey);
			bufferedOutputStream.write(13);
			bufferedOutputStream.flush();
			
			if(bufferedInputStream.available() != 0){
				int c; 
				while((c = bufferedInputStream.read()) != 13){
						
					receivedMessageInBytes.add(c);
				}
				
				receivedMessageString = MessageOps.convertBytesToString(receivedMessageInBytes);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		KVMessage kvMessage;
		
		// TODO convert receivedMessageString to KVMessage and return.
		return null;
	}

	
}
